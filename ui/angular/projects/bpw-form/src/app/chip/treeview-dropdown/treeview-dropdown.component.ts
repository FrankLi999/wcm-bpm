import {
  Component,
  OnInit,
  ViewChild,
  Input,
  Output,
  EventEmitter,
  AfterViewInit,
  ElementRef,
} from "@angular/core";
import { FlatTreeControl } from "@angular/cdk/tree";
import {
  MatTreeFlatDataSource,
  MatTreeFlattener,
} from "@angular/material/tree";
import { FormControl } from "@angular/forms";
import { COMMA, ENTER } from "@angular/cdk/keycodes";
import { MatChipInputEvent } from "@angular/material/chips";
import {
  MatAutocompleteSelectedEvent,
  MatAutocomplete,
} from "@angular/material/autocomplete";
import { Observable } from "rxjs";
import { map, startWith } from "rxjs/operators";

interface FoodNode {
  name: string;
  children?: FoodNode[];
}

const TREE_DATA: FoodNode[] = [
  {
    name: "Fruit",
    children: [{ name: "Apple" }, { name: "Banana" }, { name: "Fruit loops" }],
  },
  {
    name: "Vegetables",
    children: [
      {
        name: "Green",
        children: [{ name: "Broccoli" }, { name: "Brussels sprouts" }],
      },
      {
        name: "Orange",
        children: [{ name: "Pumpkins" }, { name: "Carrots" }],
      },
    ],
  },
];

interface ExampleFlatNode {
  expandable: boolean;
  name: string;
  level: number;
}

@Component({
  selector: "treeview-dropdown",
  templateUrl: "./treeview-dropdown.component.html",
  styleUrls: ["./treeview-dropdown.component.scss"],
})
export class TreeviewDropdownComponent implements OnInit, AfterViewInit {
  @Input() placeholder: string;
  // @Input() config: Config = new Config();
  separatorKeysCodes: number[] = [ENTER, COMMA];
  @Output() change: EventEmitter<String[]> = new EventEmitter<String[]>();

  //@ViewChild(SpTreeviewComponent) tree: SpTreeviewComponent;
  @ViewChild("selectionTree", { static: true }) tree;
  @ViewChild("chipList")
  public chipList: any;
  private chipsDiv: HTMLDivElement;
  chipCtrl = new FormControl();
  @ViewChild("chipInput") chipInput: ElementRef<HTMLInputElement>;

  visible = true;
  selectable = false;
  removable = true;
  public dropDown = false;
  filteredFruits: Observable<string[]>;
  selectedNodes: string[] = [];
  allFruits: string[] = ["Apple", "Lemon", "Lime", "Orange", "Strawberry"];
  @ViewChild("auto") matAutocomplete: MatAutocomplete;

  ////////////////////////////////////////////////////
  private _transformer = (node: FoodNode, level: number) => {
    return {
      expandable: !!node.children && node.children.length > 0,
      name: node.name,
      level: level,
    };
  };

  treeControl = new FlatTreeControl<ExampleFlatNode>(
    (node) => node.level,
    (node) => node.expandable
  );

  treeFlattener = new MatTreeFlattener(
    this._transformer,
    (node) => node.level,
    (node) => node.expandable,
    (node) => node.children
  );

  dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);

  ///////////////////////////////////////////////////
  constructor() {
    this.dataSource.data = TREE_DATA;
    this.filteredFruits = this.chipCtrl.valueChanges.pipe(
      startWith(null),
      map((fruit: string | null) =>
        fruit ? this._filter(fruit) : this.allFruits.slice()
      )
    );
  }
  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.allFruits.filter(
      (fruit) => fruit.toLowerCase().indexOf(filterValue) === 0
    );
  }
  hasChild = (_: number, node: ExampleFlatNode) => node.expandable;
  ngOnInit() {
    this.selectedNodes.push("Apple");
    // this.dropDown = this.config.showDropdownDefault;
  }

  ngAfterViewInit() {
    this.chipsDiv = this.chipList._elementRef.nativeElement.children[0];
  }

  // scrollLeft(event: Event) {
  //   event.stopPropagation();
  //   this.chipsDiv.scrollLeft -= 50;
  // }

  // scrollRight(event: Event) {
  //   event.stopPropagation();
  //   this.chipsDiv.scrollLeft += 50;
  // }

  nodeSelected(node: any) {
    if (!this.selectedNodes.includes(node.name)) {
      this.selectedNodes.push(node.name);
      this.change.emit(this.selectedNodes);
      this.chipInput.nativeElement.value = "";
      this.chipCtrl.setValue(null);
    }
  }

  remove(node: string): void {
    const index = this.selectedNodes.indexOf(node);

    if (index >= 0) {
      this.selectedNodes.splice(index, 1);
    }
  }

  add(event: MatChipInputEvent): void {
    const input = event.input;
    const value = event.value;

    // Add our fruit
    if ((value || "").trim()) {
      this.selectedNodes.push(value.trim());
    }

    // Reset the input value
    if (input) {
      input.value = "";
    }

    this.chipCtrl.setValue(null);
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.selectedNodes.push(event.option.viewValue);
    this.chipInput.nativeElement.value = "";
    this.chipCtrl.setValue(null);
  }
}
