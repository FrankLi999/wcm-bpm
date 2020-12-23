import { COMMA, ENTER } from "@angular/cdk/keycodes";
import {
  Component,
  ElementRef,
  ViewChild,
  Output,
  EventEmitter,
} from "@angular/core";
import { FormControl } from "@angular/forms";
import { MatChipInputEvent } from "@angular/material/chips";
import { FlatTreeControl } from "@angular/cdk/tree";
import {
  MatTreeFlatDataSource,
  MatTreeFlattener,
} from "@angular/material/tree";

interface ChipNode {
  name: string;
  children?: ChipNode[];
}

const TREE_DATA: ChipNode[] = [
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

interface ChipFlatNode {
  expandable: boolean;
  name: string;
  level: number;
}

/**
 * @title Chips Autocomplete
 */
@Component({
  selector: "chips-autocomplete",
  templateUrl: "./chip.component.html",
  styleUrls: ["./chip.component.scss"],
})
export class ChipComponent {
  // visible = true;
  selectable = true;
  removable = true;
  dropDown = false;
  separatorKeysCodes: number[] = [ENTER, COMMA];
  chipCtrl = new FormControl();
  @ViewChild("chipInput", { static: true }) chipInput: ElementRef<
    HTMLInputElement
  >;

  chips: string[] = ["Lemon"];
  // allFruits: string[] = ["Apple", "Lemon", "Lime", "Orange", "Strawberry"];
  @Output() change: EventEmitter<String[]> = new EventEmitter<String[]>();

  @ViewChild("selectionTree", { static: true }) tree;

  ////////////////////////////////////////////////////
  private _transformer = (node: ChipNode, level: number) => {
    return {
      expandable: !!node.children && node.children.length > 0,
      name: node.name,
      level: level,
    };
  };

  treeControl = new FlatTreeControl<ChipFlatNode>(
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
  hasChild = (_: number, node: ChipFlatNode) => node.expandable;

  nodeSelected(node: any) {
    if (!this.chips.includes(node.name)) {
      this.chips.push(node.name);
      this.change.emit(this.chips);
      this.chipInput.nativeElement.value = "";
      this.chipCtrl.setValue(null);
    }
  }

  constructor() {
    this.dataSource.data = TREE_DATA;
  }

  add(event: MatChipInputEvent): void {
    const input = event.input;
    const value = event.value;

    // Add our fruit
    if ((value || "").trim()) {
      this.chips.push(value.trim());
    }

    // Reset the input value
    if (input) {
      input.value = "";
    }

    this.chipCtrl.setValue(null);
  }

  remove(fruit: string): void {
    const index = this.chips.indexOf(fruit);

    if (index >= 0) {
      this.chips.splice(index, 1);
    }
  }
}
