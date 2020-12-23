import {
  Injectable,
  ViewContainerRef,
  ComponentFactoryResolver
} from "@angular/core";
import { BlockUIComponent } from "./block-ui.component";

@Injectable({
  providedIn: "root"
})
export class BlockUIService {
  constructor(private resolver: ComponentFactoryResolver) {}
  createBlockUIComponent(message: string, blockui: ViewContainerRef): any {
    blockui.clear();
    const factory = this.resolver.resolveComponentFactory(BlockUIComponent);
    let componentRef = blockui.createComponent(factory);
    componentRef.instance.message = message;
    return componentRef;
  }

  destroyBlockUIComponent(blockui: ViewContainerRef, componentRef: any) {
    blockui.clear();
    componentRef && componentRef.destroy();
  }
}
