import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { tap } from "rxjs/operators";
import { ProcessDefinitionsService } from "../../../../services/process-definitions.service";

@Component({
  // selector: "camunda-running-instances",
  templateUrl: "./running-instances.component.html",
  styleUrls: ["./running-instances.component.scss"],
})
export class RunningInstancesComponent implements OnInit {
  private linkBase = {
    processInstances: "/bpm/dashboard/processes/processDefinition?id={{id}}",
  };
  widget: any = {
    scheme: {
      domain: [],
    },
    values: [],
  };
  totalInstances: number = 0;
  constructor(
    private _router: Router,
    private _processDefinitionsService: ProcessDefinitionsService
  ) {}

  ngOnInit(): void {
    this._processDefinitionsService
      .getStatisticsIncludeRootIncidents(true)
      .pipe(
        tap((processDefinitionStatistics) =>
          this._aggregateInstances(processDefinitionStatistics)
        )
      )
      .subscribe();
  }

  onSelect(data): void {
    let value = this.widget.values.find((value) => value.name === data.name);
    if ("No data" !== value.name) {
      this._router.navigateByUrl(value.url);
    }
  }

  tooltipText(data) {
    return data.data.name === "No data"
      ? "No Data"
      : `${data.data.name}: ${data.data.value}`;
  }

  private _aggregateInstances(processDefinitionStatistics) {
    var values = [];
    for (let i = 0; i < processDefinitionStatistics.length; i++) {
      let statistic = processDefinitionStatistics[i];
      if (statistic.definition && statistic.instances) {
        values.push({
          value: statistic.instances,
          name: statistic.definition.name || statistic.definition.id,
          url: this._replaceAll(
            this.linkBase.processInstances,
            statistic.definition
          ),
        });
        this.totalInstances += statistic.instances;
      }
    }
    this._prepareValues(values, this.totalInstances, "/processes");
  }

  private _replaceAll(str, obj) {
    Object.keys(obj).forEach(function (searched) {
      var replaced = obj[searched];
      str = str.split("{{" + searched + "}}").join(replaced);
    });
    return str;
  }

  private _prepareValues(values, total, url) {
    var treshold = this._valuesTreshold(values.length, total);

    var belowTreshold = {
      value: 0,
      name: "Others", // $translate.instant("DASHBOARD_OTHERS"),
      // names: [],
      url: url,
    };

    values.forEach(function (item) {
      if (item.value && item.value < treshold) {
        belowTreshold.value += item.value;
        // belowTreshold.names.push(item.name);
      }
    });

    this.widget.values = values
      .filter(function (item) {
        return item.value && item.value >= treshold;
      })
      .sort(this._valuesSort);

    if (treshold) {
      this.widget.values.unshift(belowTreshold);
    }
    for (let i = 0; i < this.widget.values.length; i++) {
      this.widget.scheme.domain.push(this._color(i, this.widget.values.length));
    }

    if (this.widget.values == 0) {
      this.widget.values.push({
        name: "No data",
        value: 1,
      });
      this.widget.scheme.domain.push("#4867d2");
    }
  }

  private _valuesTreshold(length, total) {
    return length > 12 ? Math.floor(total / length) * 0.5 : 0;
  }

  private _valuesSort(a, b) {
    return a.value > b.value ? 1 : a.value < b.value ? -1 : 0;
  }

  private _color(i, t) {
    var hue = (360 / t) * i;
    hue += 230;
    hue = hue > 360 ? hue - 360 : hue;
    return "hsl(" + hue + ", 70%, 41%)";
  }
}
