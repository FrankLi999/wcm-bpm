import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { tap } from "rxjs/operators";
import { ProcessDefinitionsService } from "../../../../services/process-definitions.service";

@Component({
  //selector: "camunda-open-incidents",
  templateUrl: "./open-incidents.component.html",
  styleUrls: ["./open-incidents.component.scss"],
})
export class OpenIncidentsComponent implements OnInit {
  private linkBase = {
    processIncidents:
      "/bpm/dashboard/processes/processDefinition?id={{id}}&&detailTab=incidents",
  };
  totalIncidents: number = 0;
  widget: any = {
    scheme: {
      domain: [],
    },
    values: [],
  };
  constructor(
    private _router: Router,
    private _processDefinitionsService: ProcessDefinitionsService
  ) {}
  linkClicked() {
    this._router.navigateByUrl(
      "/bpm/dashboard/processes/processDefinition?id=aaa&&detailTab=incidents"
    );
    return false;
  }
  ngOnInit(): void {
    this._processDefinitionsService
      .getStatisticsIncludeRootIncidents(true)
      .pipe(
        tap((processDefinitionStatistics) =>
          this._aggregateIncidents(processDefinitionStatistics)
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

  private _aggregateIncidents(processDefinitionStatistics) {
    var values = [];
    for (let i = 0; i < processDefinitionStatistics.length; i++) {
      let statistic = processDefinitionStatistics[i];
      var definitionIncidents = 0;
      statistic.incidents.forEach(function (info) {
        definitionIncidents += info.incidentCount;
      });

      values.push({
        value: definitionIncidents,
        name: statistic.definition.name || statistic.definition.id,
        url: this._replaceAll(
          this.linkBase.processIncidents,
          statistic.definition
        ),
      });

      this.totalIncidents += definitionIncidents;
    }
    this._prepareValues(values, this.totalIncidents, "/processes");
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

    this.widget.values.map(function (item, i) {
      this.widget.schema.domain.push(this._color(i, this.widget.values.length));
    });

    if (this.widget.values == 0) {
      this.widget.values.push({
        name: "No data",
        value: 100,
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
