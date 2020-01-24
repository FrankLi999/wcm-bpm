# ContentChild vs ViewChild
Essentially ViewChild and ContentChild are used for component communication in Angular. Therefore, if 
a parent component wants access of child component then it uses ViewChild or ContentChild.

Any component, directive, or element which is part of a template is ViewChild and any 
component or element which is projected in the template is ContentChild.


    @Component({
        selector: 'todo-app',
        template: `
            <section>
            Add todo:
            <todo-input (todo)="addTodo($event)"></todo-input>
            </section>
            <section>
            <h4 *ngIf="todos.getAll().length">Todo list</h4>
            <todo-item *ngFor="let todo of todos.getAll()" [todo]="todo">
            </todo>
            </section>
            <ng-content select="app-footer"></ng-content> 
        `
    })
    class TodoAppComponent {
        constructor(private todos: TodoList) {}
        addTodo(todo) {
            this.todos.add(todo);
        }
    }

    Content child: <ng-content select="app-footer"></ng-content> 
    viewchild: <todo-input (todo)="addTodo($event)"></todo-input>   

## ViewChild and ViewChildren
    If you want to access following inside the Parent Component, use @ViewChild decorator of Angular.

    Child Component
    Directive
    DOM Element

    We can do following tasks:

    > Import ViewChild and AfterViewInit from @angular/core
    > Implement AfterViewInit life cycle hook to component class
    > Create a variable with decorator @ViewChild or @ViewChildren
    > Access that inside ngAfterViewInit life cycle hook

    import { Component, Input } from '@angular/core';
    @Component({
        selector: 'app-message',
        template: `
    <h2>{{message}}</h2>
    
    `
    })
    export class MessageComponent {
        @Input() message: string;
    
    }

    then, 
### Change the property value of child
    > By changing the ViewChild property in ngAfterContentInit life cycle hook. The only problem with this approach is when you work with more than one ViewChild also known as ViewChildren. Reference of ViewChildren is not available in ngAfterContnetInit life cycle hook.

    > Manually calling change detection using ChangeDetectorRef
        > Import ChangeDetectorRef from @angular/core
        > Inject it to the constructor of Component class
        > Call detectChanges() method after ViewChild property is changed

            constructor(private cd: ChangeDetectorRef) {}    
            ngAfterViewInit() {
                console.log(this.messageViewChild);
                this.messageViewChild.message = 'Passed as View Child';
                this.cd.detectChanges();
            }

            or 

            @ViewChildren(MessageComponent, {static: false}) messageViewChildren: QueryList<MessageComponent>;
            constructor(private cd: ChangeDetectorRef) {
            }
            ngAfterViewInit() {
                console.log(this.messageViewChildren);
                this.messageViewChildren.forEach((item) => { item.message = 'Infragistics'; });
                this.cd.detectChanges();
            }
### Querying Standard HTML Elements with Template References
    <hello name=""></hello>

    <p #pRef>
    Start editing to see some magic happen :)
    </p>

    @ViewChild('pRef', {static: false}) pRef: ElementRef;
    ngAfterViewInit() {
        console.log(this.pRef.nativeElement.innerHTML); 
        this.pRef.nativeElement.innerHTML = "DOM updated successfully!!!"; 
    }
## ContentChild and ContnetChildren           
    To work with ContentChildren and QueryList, you need to do following tasks:

        > Import ContentChildren , QueryList , AfterContentInit from @angular/core
        >  Make reference of ContnetChildren with type QueryList
        > Access ContentChildren reference in ngAfterContentInit() life cycle hook

    import { Component, OnInit } from '@angular/core';
    @Component({
        selector: 'app-root',
        template: `
    
        <div>
            <app-messagecontainer>
            <app-message *ngFor='let m of messages' [message]='m'></app-message>
            </app-messagecontainer>
        </div>
    
    `
    })
    export class AppComponent implements OnInit {
        messages: any;
        ngOnInit() {
            this.messages = this.getMessage();
        }
        getMessage() {
            return [
                'Hello India',
                'Which team is winning Super Bowl? ',
                'Have you checked Ignite UI ?',
                'Take your broken heart and make it to the art'
            ];
        }
    }

    /////////////////////////////////////////////////////////
    import { Component, ContentChild, AfterContentInit } from '@angular/core';
    import { MessageComponent } from './message.component';
    
    @Component({
        selector: 'app-messagecontainer',
        template: `
    
    <div>
    
    <h3>{{greetMessage}}</h3>
    
        <ng-content select="app-message"></ng-content>
        </div>
    
        `
    })
    // export class MessageContainerComponent implements AfterContentInit {
    //    greetMessage = 'Ignite UI Rocks!';
    //    @ContentChild(MessageComponent) MessageComponnetContentChild: MessageComponent;
    //    ngAfterContentInit() {
    //        console.log(this.MessageComponnetContentChild);
    //    }
    // }

    export class MessageContainerComponent implements AfterContentInit {
        greetMessage = 'Ignite UI Rocks!';
        @ContentChildren(MessageComponent) messageComponnetContentChildren: QueryList<MessageComponent>;
        ngAfterContentInit() {
            console.log(this.messageComponnetContentChildren);
        }
    }

    You can query each item in ContentChildren and modify property as shown in the listing below:

        ngAfterContentInit() {
            this.MessageComponnetContentChild.forEach((m) => m.message = 'Foo');
        }

# Angular ng-content and Content Projection
    @Component({
        selector: 'fa-input',
        template: `
            <i class="fa" [ngClass]="classes"></i>
            <ng-content></ng-content>
        `,
        styleUrls: ['./fa-input.component.css']
    })
    export class FaInputComponent {

        @Input() icon: string;

        get classes() {
            const cssClasses = {
            fa: true
            };
            cssClasses['fa-' + this.icon] = true;
            return cssClasses;
        }
    }
## How To apply styles to elements projected via ng-content
    :host ::ng-deep input {
        border: none;
        outline: none;
    }

## Interacting with Projected Content (inside ng-content)
    @Component({
        selector: 'fa-input',
        template: `
            <i class="fa" [ngClass]="classes"></i>
            <ng-content></ng-content>
        `,
        styleUrls: ['./fa-input.component.css']
    })
    export class FaInputComponent {

        @Input() icon: string;

        @ContentChild(InputRefDirective)
        input: InputRefDirective;

        @HostBinding("class.focus")
        get focus() {
            return this.input ? this.input.focus : false;
        }

        get classes() {
            const cssClasses = {
                fa: true
            };
            cssClasses['fa-' + this.icon] = true;
            return cssClasses;
        }
    }

    @Directive({
        selector: '[inputRef]'
    })
    export class InputRefDirective {
        focus = false;

        @HostListener("focus")
        onFocus() {
            this.focus = true;
        }

        @HostListener("blur")
        onBlur() {
            this.focus = false;
        }
    }

## Multi-Slot Content Projection
        @Component({
            selector: 'fa-input',
        template: `
            <ng-content select="i"></ng-content>
            <ng-content select="input"></ng-content>
        `})
        export class FaInputComponent {
            ..
        }

    or

        @Component({
            selector: 'fa-input',
        template: `
            <ng-content select="i"></ng-content>
            <ng-content></ng-content>
        `})
        export class FaInputComponent {
            ...
        }

        <!-- inside container component -->
        <ng-content select="[slot='one']"></ng-content>
        <ng-content select="[slot='two']"></ng-content>

        <!-- inside another component using container component -->
        <container-component>
            <p slot="one">Content For Slot one</p>
            <p slot="two">Content For Slot two</p>
        </container-component>

    or 

        <!-- inside container component -->
        <!-- Element -->
        <ng-content select="slot-one"></ng-content>
        <!-- Attributes -->
        <ng-content select="[slot-one]"></ng-content>
        <ng-content select="[slot][two]"></ng-content>
        <!-- Attributes with Value -->
        <ng-content select="[slot='one']"></ng-content>
        <ng-content select="[slot='two']"></ng-content>
        <!-- Inside ngProjectAs use projected name-->
        <ng-container ngProjectAs="slot-one">
        Very <strong>important</strong> text with tags.
        </ng-container>
        <ng-container ngProjectAs="[slot][two]">
        Very <strong>important</strong> text with tags.
        </ng-container>

    Inside *ngFor
        // Using the key $implicit in the context object will set its value as default.
        // Container Component
        @Component({
        ...
        template: `
            <li *ngFor="let item of items">
            <ng-container [ngTemplateOutlet]="templateRef" [ngTemplateOutletContext]="{$implicit: item}"></ng-container>
            </li>
        `
        })
        class TabsComponent {
        @ContentChild(TemplateRef) templateRef:TemplateRef;
        @Input() items;
        }
        <!-- data here is the array  input for container-component -->
        <container-component [items]="data">
        <ng-template let-item>
            <!-- here we can use item -->
            {{ item }}
        </ng-template>
        </container-component>
# ng-template, ng-container and ngTemplateOutlet
    Angular is already using ng-template under the hood in many of the structural directives that we use all the time: ngIf, ngFor and ngSwitch.

        <div class="lessons-list" *ngIf="lessons else loading">
            ... 
        </div>

        <ng-template #loading>
            <div>Loading...</div>
        </ng-template>


## Multiple Structural Directives
    <div *ngIf="lessons">
        <!-- extra div here -->
        <div class="lesson" *ngFor="let lesson of lessons">
            <div class="lesson-detail">
                {{lesson | json}}
            </div>
        </div>
    </div>

    <ng-container *ngIf="lessons">
        <div class="lesson" *ngFor="let lesson of lessons">
            <div class="lesson-detail">
                {{lesson | json}}
            </div>
        </div>
    </ng-container>
## Dynamic Template Creation with the ngTemplateOutlet directive
    <ng-container *ngTemplateOutlet="loading"></ng-container>
## Template Context
    Inside the ng-template tag body, we have access to the same context variables that are visible in the outer template. And this is because all ng-template instances have access also to the same context on which they are embedded.

    But each template can also define its own set of input variables! Actually, each template has associated a context object containing all the template-specific input variables.
    @Component({
        selector: 'app-root',
        template: `      
        <ng-template #estimateTemplate let-lessonsCounter="estimate">
            <div> Approximately {{lessonsCounter}} lessons ...</div>
        </ng-template>
        <ng-container 
        *ngTemplateOutlet="estimateTemplate;context:ctx">
        </ng-container>
    `})
    export class AppComponent {

        totalEstimate = 10;
        ctx = {estimate: this.totalEstimate};
    
    }

## Template References
    @Component({
        selector: 'app-root',
        template: `      
            <ng-template #defaultTabButtons>
                <button class="tab-button" (click)="login()">
                    {{loginText}}
                </button>
                <button class="tab-button" (click)="signUp()">
                    {{signUpText}}
                </button>
            </ng-template>
    `})
    export class AppComponent implements OnInit {

        @ViewChild('defaultTabButtons')
        private defaultTabButtonsTpl: TemplateRef<any>;

        ngOnInit() {
            console.log(this.defaultTabButtonsTpl);
        }

    }
## Configurable Components with Template Partial @Inputs
    @Component({
        selector: 'app-root',
        template: `      
        <ng-template #customTabButtons>
            <div class="custom-class">
                <button class="tab-button" (click)="login()">
                {{loginText}}
                </button>
                <button class="tab-button" (click)="signUp()">
                {{signUpText}}
                </button>
            </div>
        </ng-template>
        <tab-container [headerTemplate]="customTabButtons"></tab-container>      
    `})
    export class AppComponent implements OnInit {

    }
# Angular Injection tokens
    Injection tokens are a feature of Angular that allows the injection of values that don't have a runtime representation. 

    You can't inject something like an interface as it only exists as a TypeScript construct, not JavaScript. Injection tokens provide a simple mechanism to link a token, to a value, and have that value injected into a component.

## Inject string value
        import { InjectionToken } from '@angular/core';
        export const MY_TOKEN = new InjectionToken<string>('MyToken');

        @NgModue({
            providers: [
                { provide: MY_TOKEN, useValue: 'Hello world' }
            ]
        })
        export class AppModule { }

        @Component({
            selector: 'my-component',
            template: '<h1>{{ value }}</h1>'
        })
        export class MyComponent {
            constructor(@Inject(MY_TOKEN) public value: string) { }
        }
## Inject interface
    export interface MenuItem {
        label: string;
        route: any[];
    }

    export const SETTINGS_MENU = new InjectionToken<MenuItem>('Settings');

    const moduleOneMenu: MenuItem = {
        label: 'Module one',
        route: ['/module-one']
    };

    @NgModule({
        providers: [
            { provide: SETTINGS_MENU, useValue: moduleOneMenu, multi: true }
        ]
    })
    export class ModuleOne { }

    const moduleTwoMenu: MenuItem = {
        label: 'Module two',
        route: ['/module-two']
    };

    @NgModule({
        providers: [
            { provide: SETTINGS_MENU, useValue: moduleTwoMenu, multi: true }
        ]
    })
    export class ModuleTwo { }
    
    @Component({
        selector: 'my-menu',
        template: `
            <ul>
            <li *ngFor="let item of items">
                <a [routeLink]="item.route">{{ item.label }}</a>
            </li>
            </ul>`
    })
    export class MenuComponent {
        constructor(@Inject(SETTINGS_MENU) public items: MenuItem[]) { }
    }
# ViewEncapsulation

## Shadow DOM:
In a simple word, Shadow DOM will allow us to apply Scoped Styles to elements without affecting other elements.

## ViewEncapsulation Types
Emulated: 0, no shadow dom but provide style encapsulation by adding new host
  element attribute to all selectors.

  h1[_ngContent-c0] : {
      ...
  }
  <h1 _ngContent-c0> ..</h1>
None: 2, no shadow dom
ShadowDom: 3,  use shadow dom for style encapsulation

# Change detection
https://vsavkin.com/change-detection-in-angular-2-4f216b855d4c
A Change: changing the property of a component or emitting an event.

Every component gets a change detector responsible for checking the bindings defined in its template. Examples of bindings: {{todo.text}} and [todo]=”t”. Change detectors propagate bindings from the root to leaves in the depth first order.

## ChangeDetectionStrategy

ChangeDetectionStrategy.Default
    By default, Angular makes no assumption on what the component depends upon. So it has to be conservative and will checks every time something may have changed, this is called dirty checking. In a more concrete way, it will perform checks for each browser events, timers, XHRs and promises.

ChangeDetectionStrategy.onPush
    OnPush works by comparing references of the inputs of the component, so
    Avoiding direct object mutation with OnPush

    Immutible Objcet:
        The only thing the OnPush strategy disallows is depending on shared mutable state.

    Observable Objects
        If a component depends only on its input properties, and they are observable, then this component can change if and only if one of its input properties emits an event. Therefore, we can skip the component’s subtree in the change detection tree until such an event occurs. When it happens, we can check the subtree once, and then disable it until the next change.

        it is quite different with the immutible Objcet case. If you have a tree of components with immutable bindings, a change has to go through all the components starting from the root. This is not the case when dealing with observables.

    With onPush, the component only depends on its inputs and embraces the immutability, the change detection strategy will kicks in when:
        > The Input reference changes;
        > An event originated from the component or one of its children;
        > Run change detection explicitly (componentRef.markForCheck());
        > Use the async pipe in the view.

    > OnPush and share Observables via service : child component will get the updates.
    > Passing Observables as @Inputs() to a OnPush component

    However, manually subscribes to the observable in ngOnInit will only work with the default change detection mechanism, but not with OnPush.  we simply need to make sure that any observables that we inject directly via constructor services are subscribed to at the template level using the async pipe
## How is change detection implemented?
    Angular will replace addEventListener with a new version that does the equivalent of this:

    // this is the new version of addEventListener
    function addEventListener(eventName, callback) {
        // call the real addEventListener
        callRealAddEventListener(eventName, function() {
            // first call the original callback
            callback(...);     
            // and then run Angular-specific functionality
            var changed = angular2.runChangeDetection();
            if (changed) {
                angular2.reRenderUIPart();
            }
     });

## How does this low-level runtime patching work?
    This low-level patching of browser APIs is done by a library shipped with Angular called Zone.js.

    Browser Async APIs supported
    many other browser APIs are patched by Zone.js to transparently trigger Angular change detection, such as for example Websockets. 

        > all browser events (click, mouseover, keyup, etc.)
        > setTimeout() and setInterval()
        > Ajax requests

    One limitation of this mechanism is that if by some reason an asynchronous browser API is not supported by Zone.js, then change detection will not be triggered. This is, for example, the case of IndexedDB callbacks.

## How does the default change detection mechanism work

By default, Angular Change Detection works by checking if the value of template expressions have changed. This is done for all components.

By default, Angular does not do deep object comparison to detect changes, it only takes into account properties used by the template

Compare by reference(none onPush).

Take todo list example, see how the two new buttons behave:

    the first button "Toggle First Item" does not work! This is because the toggleFirst() method directly mutates an element of the list. TodoList cannot detect this, as its input reference todos did not change

    the second button does work! notice that the method addTodo() creates a copy of the todo list, and then adds an item to the copy and finally replaces the todos member variable with the copied list. This triggers change detection because the component detects a reference change in its input: it received a new list!
    
    In the second button, mutating directly the todos list will not work! we really need a new list.


## turning on/off change detection, and triggering it manually
There could be special occasions where we do want to turn off change detection. Imagine a situation where a lot of data arrives from the backend via a websocket. We might want to update a certain part of the UI only once every 5 seconds. To do so, we start by injecting the change detector into the component:

  constructor(private ref: ChangeDetectorRef) {
    ref.detach();
    setInterval(() => {
      this.ref.detectChanges();
    }, 5000);
  }
## Avoiding change detection loops: Production vs Development mode
### How to trigger a change detection loop in Angular?

One way is if we are using lifecycle callbacks. For example in the TodoList component we can trigger a callback to another component that changes one of the bindings:

    ngAfterViewChecked() {
        if (this.callback && this.clicked) {
            console.log("changing status ...");
            this.callback(Math.random());
        }
    }

An error message will show up in the console:

EXCEPTION: Expression '{{message}} in App@3:20' has changed after it was checked
This error message is only thrown if we are running Angular in development mode. What happens if we enable production mode?

In production mode, the error would not be thrown and the issue would remain undetected.



## What does the OnPush change detection mode actually do?
@Component({
    selector: 'todo-list',
    changeDetection: ChangeDetectionStrategy.OnPush,
    template: ...
})
export class TodoList {
    ...
}

## Is OnPush really just comparing inputs by reference?
This is not the case, if you try to toggle a todo by clicking on it, it still works! Even if you switch TodoItem to OnPush as well. This is because
OnPush does not check only for changes in the component inputs: if a component emits an event that will also trigger change detection.

According to this quote from Victor Savkin in his blog:

When using OnPush detectors, then the framework will check an OnPush component when any of its input properties changes, when it fires an event, or when an Observable fires an event

Although allowing for better performance, the use of OnPush comes at a high complexity cost if used with mutable objects. It might introduce bugs that are hard to reason about and reproduce. Consider immutable

https://blog.angular-university.io/onpush-change-detection-how-it-works/
## Using Immutable.js to simplify the building of Angular apps
https://blog.angular-university.io/angular-2-application-architecture-building-flux-like-apps-using-redux-and-immutable-js-js/
With an immutable object, we have the guarantee that:

    > a new immutable object will always trigger OnPush change detection
    > we cannot accidentally create a bug by forgetting to create a new copy of an    object because the only way to modify data is to create new objects

## Custom elements
## Shadow DOM
        Shadow DOM allows hidden DOM trees to be attached to elements in the regular DOM tree — this shadow DOM tree starts with a shadow root, underneath which can be attached to any elements you want, in the same way as the normal DOM.

        you take advantage of its benefits (CSS scoping, DOM encapsulation, composition) and build reusable custom elements, which are resilient, highly configurable, and extremely reusable.


            let shadow = elementRef.attachShadow({mode: 'open'});
            let shadow = elementRef.attachShadow({mode: 'closed'})
        open means that you can access the shadow DOM using JavaScript written in the main page context, for example using the Element.shadowRoot property:

            let myShadowDom = myCustomElem.shadowRoot;

            var para = document.createElement('p');
            shadow.appendChild(para);
        
            class PopUpInfo extends HTMLElement {
                constructor() {
                    // Always call super first in constructor
                    super();

                    // write element functionality in here

                    // Create a shadow root
                    var shadow = this.attachShadow({mode: 'open'});

                    // Create spans
                    var wrapper = document.createElement('span');
                    wrapper.setAttribute('class','wrapper');
                    var icon = document.createElement('span');
                    icon.setAttribute('class','icon');
                    icon.setAttribute('tabindex', 0);
                    var info = document.createElement('span');
                    info.setAttribute('class','info');

                    // Take attribute content and put it inside the info span
                    var text = this.getAttribute('text');
                    info.textContent = text;

                    // Insert icon
                    var imgUrl;
                    if(this.hasAttribute('img')) {
                    imgUrl = this.getAttribute('img');
                    } else {
                    imgUrl = 'img/default.png';
                    }
                    var img = document.createElement('img');
                    img.src = imgUrl;
                    icon.appendChild(img);

                    // Create some CSS to apply to the shadow dom
                    var style = document.createElement('style');

                    style.textContent = `
                    .wrapper {
                        position: relative;
                    }

                    .info {
                        font-size: 0.8rem;
                        width: 200px;
                        display: inline-block;
                        border: 1px solid black;
                        padding: 10px;
                        background: white;
                        border-radius: 10px;
                        opacity: 0;
                        transition: 0.6s all;
                        position: absolute;
                        bottom: 20px;
                        left: 10px;
                        z-index: 3;
                    }

                    img {
                        width: 1.2rem;
                    }

                    .icon:hover + .info, .icon:focus + .info {
                        opacity: 1;
                    }`;

                    // attach the created elements to the shadow dom
                    shadow.appendChild(style);
                    shadow.appendChild(wrapper);
                    wrapper.appendChild(icon);
                    wrapper.appendChild(info);
                }
            }

            using the custom element:
                // Define the new element
                customElements.define('popup-info', PopUpInfo);

                <popup-info img="img/alt.png" text="Your card validation code (CVC) is an extra 
                                    security feature — it is the last 3 or 4
                                    numbers on the back of your card.">


            ///////////////////////////////////////////////////////////////////////

            Shadow DOM fixes CSS and DOM. It introduces scoped styles to the web platform. Without tools or naming conventions, you can bundle CSS with markup, hide implementation details, and author self-contained components in vanilla JavaScript.

            ///////////////////////////////////////////////////////////////////////
            <span class="shadow-host">
                <a href="https://twitter.com/ireaderinokun">
                    Follow @ireaderinokun
                </a>
            </span>

            const shadowEl = document.querySelector(".shadow-host");
            const shadow = shadowEl.attachShadow({mode: 'open'});

            const link = document.createElement("a");
            link.href = shadowEl.querySelector("a").href;
            link.innerHTML = `
                <span aria-label="Twitter icon"></span> 
                ${shadowEl.querySelector("a").textContent}
            `;

            shadow.appendChild(link);

            styles.textContent = `
                a, span {
                    vertical-align: top;
                    display: inline-block;
                    box-sizing: border-box;
                }

                a {
                    height: 20px;
                    padding: 1px 8px 1px 6px;
                    background-color: #1b95e0;
                    color: #fff;
                    border-radius: 3px;
                    font-weight: 500;
                    font-size: 11px;
                    font-family:'Helvetica Neue', Arial, sans-serif;
                    line-height: 18px;
                    text-decoration: none;   
                }

                a:hover {  background-color: #0c7abf; }

                span {
                    position: relative;
                    top: 2px;
                    width: 14px;
                    height: 14px;
                    margin-right: 3px;
                    background: transparent 0 0 no-repeat;
                    background-image: url(data:image/svg+xml,%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20viewBox%3D%220%200%2072%2072%22%3E%3Cpath%20fill%3D%22none%22%20d%3D%22M0%200h72v72H0z%22%2F%3E%3Cpath%20class%3D%22icon%22%20fill%3D%22%23fff%22%20d%3D%22M68.812%2015.14c-2.348%201.04-4.87%201.744-7.52%202.06%202.704-1.62%204.78-4.186%205.757-7.243-2.53%201.5-5.33%202.592-8.314%203.176C56.35%2010.59%2052.948%209%2049.182%209c-7.23%200-13.092%205.86-13.092%2013.093%200%201.026.118%202.02.338%202.98C25.543%2024.527%2015.9%2019.318%209.44%2011.396c-1.125%201.936-1.77%204.184-1.77%206.58%200%204.543%202.312%208.552%205.824%2010.9-2.146-.07-4.165-.658-5.93-1.64-.002.056-.002.11-.002.163%200%206.345%204.513%2011.638%2010.504%2012.84-1.1.298-2.256.457-3.45.457-.845%200-1.666-.078-2.464-.23%201.667%205.2%206.5%208.985%2012.23%209.09-4.482%203.51-10.13%205.605-16.26%205.605-1.055%200-2.096-.06-3.122-.184%205.794%203.717%2012.676%205.882%2020.067%205.882%2024.083%200%2037.25-19.95%2037.25-37.25%200-.565-.013-1.133-.038-1.693%202.558-1.847%204.778-4.15%206.532-6.774z%22%2F%3E%3C%2Fsvg%3E);
                }
            `;

            shadow.appendChild(styles);

## HTML templates
## HTML Import
# zone.js

A zone is nothing more than an execution context that survives multiple Javascript VM execution turns. It's a generic mechanism which we can use to add extra functionality to the browser. Angular uses Zones internally to trigger change detection, but another possible use would be to do application profiling, or keeping track of long stack traces that run across multiple VM turns.

Have a look at the Zone.js test specifications to see what is currently supported.
https://github.com/angular/zone.js/tree/master/test/patch

## zone.js performance pack
# AOT
    https://angular.io/guide/aot-metadata-errors
    https://medium.com/sparkles-blog/angular-writing-aot-friendly-applications-7b64c8afbe3f
    The central part of Angular is its compiler.
    
    The compilation can be done just in time (at runtime) and ahead of time (as a build step).

    To make AOT work the application has to have a clear separation of the static and dynamic data in the application. And the compiler has to built in such a way that it only depends on the static data.

    For Angular to be able to compile your application ahead of time, the metadata has to be statically analyzable.

    The following will not work because of window.hide is not define.

    @Component({
        selector: 'talk-cmp',
        template: () => window.hide ? 'hidden' : `
            {{talk.title}} {{talk.speaker}}
            Rating: {{ talk.rating | formatRating }}
            <watch-button [talk]="talk"></watch-button>
            <rate-button [talk]="talk" (click)="onRate()"></rate-button>
        `
    })
    class TalkCmp {
        //...
    }

## Metadata restrictions
    You write metadata in a subset of TypeScript that must conform to the following general constraints:

    Limit expression syntax to the supported subset of JavaScript.
    Only reference exported symbols after code folding.
    Only call functions supported by the compiler.
    Decorated and data-bound class members must be public.


    No arrow functions in a metadata expression
        The AOT compiler does not support function expressions and arrow functions, also called lambda functions.

        Consider the following component decorator:

        content_copy
        @Component({
        ...
        providers: [{provide: server, useFactory: () => new Server()}]
        })
    Code folding: such as resolve 1+2+3+4 as 10.


# Template
## Template reference variable/define local var with #
<confirmation-dialog #dialog></confirmation-dialog> 
<button (click)="dialog.open()">Open</button>

## define embedded template with * 
Angular treats template elements in a special way. They are used to create views, chunks of DOM you can dynamically manipulate. The * syntax is a shortcut that lets you avoid writing the whole element. 

## HTML attribute vs. DOM property
The distinction between an HTML attribute and a DOM property is key to understanding how Angular binding works. Attributes are defined by HTML. Properties are accessed from DOM (Document Object Model) nodes.

    > A few HTML attributes have 1:1 mapping to properties; for example, id.
    > Some HTML attributes don't have corresponding properties; for example, aria-*.
    > Some DOM properties don't have corresponding attributes; for example, 
      textContent.

      HTML attribute:
      <button disabled>Save</button>

      Angular template:
      <button [disabled]="isUnchanged">Save</button>
      
      Notice that the binding is to the disabled property of the button's DOM element, not the attribute. This applies to data-binding in general. Data-binding works with properties of DOM elements, components, and directives, not HTML attributes.

      In Angular, the only role of HTML attributes is to initialize element and directive state.

        Template binding works with properties and events, not attributes.

         Attributes initialize DOM properties and then they are done. Property values can change; attribute values can't.

         There is one exception to this rule. Attributes can be changed by setAttribute(), which re-initializes corresponding DOM properties.

         <TD> properties: HTMLTableCellElement, https://developer.mozilla.org/en-US/docs/Web/API/HTMLTableCellElement

         <TD> element attributes: https://developer.mozilla.org/en-US/docs/Web/HTML/Element/td

            <input [disabled]="condition ? true : false">
            <input [attr.disabled]="condition ? 'disabled' : null">

            Generally, use property binding over attribute binding as it is more intuitive (being a boolean value), has a shorter syntax, and is more performant.

## let- prefix

# moduleId: module.id of the component
  it is used to resolve relative paths for your stylesheets and templates.Module ID of the module that contains the component. We had to be able to resolve relative URLs for templates styles. In Dart, this can be determined automatically and it is not necessary to configure it. In CommonJS or SystemJS, this can always be set in module.id.

    It is strongly recommended to write only paths related to the components. This is the only form of URL discussed in these documents. You no longer need to write @Component ({moduleId: module.id})
    
# Angular security
## preventing cross-site scripting (XSS)
        When a value is inserted into the DOM from a template, via property, attribute, style, class binding, or interpolation, Angular sanitizes and escapes untrusted values.

        export enum SecurityContext { NONE, HTML, STYLE, SCRIPT, URL, RESOURCE_URL }

        export abstract class DomSanitizer implements Sanitizer {
            abstract sanitize(context: SecurityContext, value: SafeValue|string|null): string|null;
            abstract bypassSecurityTrustHtml(value: string): SafeHtml;
            abstract bypassSecurityTrustStyle(value: string): SafeStyle;
            abstract bypassSecurityTrustScript(value: string): SafeScript;
            abstract bypassSecurityTrustUrl(value: string): SafeUrl;
            abstract bypassSecurityTrustResourceUrl(value: string): SafeResourceUrl;
        }

    HOW CAN WE DISABLE THE SANITIZATION LOGIC
        import {BrowserModule, DomSanitizer} from '@angular/platform-browser'

        @Component({
            selector: 'my-app',
            template: `
                <div [innerHtml]="html"></div>
            `,
        })
        export class App {
            constructor(private sanitizer: DomSanitizer) {
                this.html = sanitizer.bypassSecurityTrustHtml('<h1>DomSanitizer</h1><script>ourSuperSafeCode()</script>') ;
            }
        }
 ### CONTENT SECURITY POLICY (CSP)    
    Content Security Policy (CSP) is an added layer of security that helps to detect and mitigate certain types of attacks, including Cross Site Scripting (XSS) and data injection attacks.

    To enable CSP, configure your web server to return an appropriate Content-Security-Policy HTTP header. You can find a very detailed manual how to enable CSP on the MDN website ( https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP ). To check if your CSP is valid you can use the CSP evaluator from google (https://csp-evaluator.withgoogle.com/).

     If the site doesn't offer the CSP header, browsers likewise use the standard same-origin policy.

     Alternatively, the <meta> element can be used to configure a policy, for example: <meta http-equiv="Content-Security-Policy" content="default-src 'self'; img-src https://*; child-src 'none';">
###  USE THE OFFLINE TEMPLATE COMPILER (AKA AOT-COMPILER)
###  AVOID DIRECT USE OF THE DOM APIS 
###  SERVER-SIDE XSS PROTECTION
    validate all data on server-side code and escape appropriately to prevent XSS vulnerabilities on the server. Also, Angular recommends not to generate Angular templates on the server side using a templating language.
##  HTTP-LEVEL VULNERABILITIES

Angular has built-in support to help prevent two common HTTP vulnerabilities, cross-site request forgery (CSRF or XSRF) and cross-site script inclusion (XSSI). Both of these must be mitigated primarily on the server side, but Angular provides helpers to make integration on the client side easier.

### CROSS-SITE REQUEST FORGERY (XSRF)
Cross-site request forgery (also known as one-click attack or session riding) is abbreviated as CSRF or XSRF. It is a type of malicious exploit of a website where unauthorized commands are transmitted from a user that the web application trusts.

 Angular HttpClient supports a common mechanism used to prevent XSRF attacks. When performing HTTP requests, an interceptor reads a token from a cookie, by default XSRF-TOKEN, and sets it as an HTTP header, X-XSRF-TOKEN. Since only code that runs on your domain could read the cookie, the backend can be certain that the HTTP request came from your client application and not an attacker.

By default, an interceptor sends this header on all mutating requests (such as POST) to relative URLs, but not on GET/HEAD requests or on requests with an absolute URL.

To take advantage of this, your server needs to set a token in a JavaScript readable session cookie called XSRF-TOKEN on either the page load or the first GET request. On subsequent requests the server can verify that the cookie matches the X-XSRF-TOKEN HTTP header, and therefore be sure that only code running on your domain could have sent the request. The token must be unique for each user and must be verifiable by the server; this prevents the client from making up its own tokens. Set the token to a digest of your site's authentication cookie with a salt for added security.

Configuring custom cookie/header names
    mports: [
        HttpClientModule,
        HttpClientXsrfModule.withOptions({
            cookieName: 'My-Xsrf-Cookie',
            headerName: 'My-Xsrf-Header',
    }),
### CROSS-SITE SCRIPT INCLUSION (XSSI)
Cross-site script inclusion (also known as JSON vulnerability) can allow an attacker’s website to read data from a JSON API. The attack works on older browsers by overriding native JavaScript object constructors, and then including an API URL using a <script> tag. This attack is only successful if the returned JSON is executable as JavaScript.

Servers can prevent an attack by prefixing all JSON responses to make them non-executable, by convention, using the well-known string ")]}',\n". Angular’s HttpClient library recognizes this convention and automatically strips the string ")]}',\n" from all responses before further parsing.

# plug and play
https://blog.angularindepth.com/building-extensible-dynamic-pluggable-enterprise-application-with-angular-aed8979faba5


# angular cli command line option
# @schematics/angular

# ivy
https://www.softwarearchitekt.at/aktuelles/architecture-with-ivy-a-possible-future-without-angular-modules/

https://www.softwarearchitekt.at/aktuelles/architecture-with-angular-ivy-part-2-higher-order-and-dynamic-components/
https://github.com/manfredsteyer/ivy-dynamic-components-hoc
# Dynamic route
# Flex
# Angular pwa
# browser side performance 
https://github.com/mgechev/angular-performance-checklist

# Angular Web_Components support 
        https://developer.mozilla.org/en-US/docs/Web/Web_Components
# trigger debugger 
//debugger;
# Flux and Redux
https://github.com/petehunt/react-howto/issues/12

# Anular styles:
	 LIFT: Do structure the app such that you can Locate code 
		quickly, Identify the code at a glance, keep the Flattest 
		structure you can, and Try to be DRY.
	 creating sub-folders when a folder reaches seven or more files.
	 Consider creating a folder for a component when it has multiple 
		accompanying files (.ts, .html, .css and .spec).
	Folders-by-feature structure
	create an NgModule for all distinct features in an application
	Do create a feature module named SharedModule in a shared folder
	
	Member sequence

		Do place properties up top followed by methods.
		Do place private members after public members, alphabetized.
	HostListener/HostBinding decorators versus host metadata
		preferring the @HostListener and @HostBinding to the 
		host property of the @Directive and @Component 
		decorators.
		
			import { Directive, HostBinding, HostListener } from '@angular/core';

			@Directive({
			  selector: '[tohValidator]'
			})
			export class ValidatorDirective {
			  @HostBinding('attr.role') role = 'button';
			  @HostListener('mouseenter') onMouseEnter() {
				// do work
			  }
			}
			
			vs
			
			import { Directive } from '@angular/core';

			@Directive({
			  selector: '[tohValidator2]',
			  host: {
				'[attr.role]': 'role',
				'(mouseenter)': 'onMouseEnter()'
			  }
			})
			export class Validator2Directive {
			  role = 'button';
			  onMouseEnter() {
				// do work
			  }
			}
	Do provide a service with the app root injector in the 
		@Injectable decorator of the service.
		The Angular injector is hierarchical.
			@Injectable({
			  providedIn: 'root',
			})
			export class Service {
			}
	Do use the @Injectable() class decorator instead of the 
		@Inject parameter decorator when using types as 
		tokens for the dependencies of a service.
		
		use 
		@Injectable()
        export class HeroArena {
            constructor(
            private heroService: HeroService,
            private http: HttpClient) {}
        }

		instead of 
		export class HeroArena {
		  constructor(
			  @Inject(HeroService) private heroService: HeroService,
			  @Inject(HttpClient) private http: HttpClient) {}
		}
	Codelyzer: Do use codelyzer to follow this guide.	
	File templates and snippets:
	    Consider using snippets for Visual Studio Code that 
		follow these styles and guidelines.