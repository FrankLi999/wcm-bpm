### References

https://www.sitepoint.com/react-with-typescript-best-practices/
https://www.robertcooper.me/using-eslint-and-prettier-in-a-typescript-project

https://github.com/typescript-cheatsheets/react
https://www.typescriptlang.org/docs/handbook/compiler-options.html

type vs interface

https://medium.com/@martin_hotell/interface-vs-type-alias-in-typescript-2-7-2a8f1777af4c

1. you cannot use implements on an class with type alias if you use union operator within your type definition

2. you cannot use extends on an interface with type alias if you use union operator within your type definition

3. declaration merging doesn’t work with type alias

### Props

The next core concept we’ll cover is props. You can define your props using either an interface or a type.

When it comes to types or interfaces, we suggest following the guidelines presented by the react-typescript-cheatsheet community:

> always use interface for public API’s definition when authoring a library or 3rd-party ambient type definitions.”
> consider using type for your React Component Props and State, because it is

### Hooks

The TypeScript type inference works well when using hooks.

On the rare occasions where you need to initialize a hook with a null-ish value, you can make use of a generic and pass a union to correctly type your hook.

type User = {
email: string;
id: string;
}

// the generic is the < >
// the union is the User | null
// together, TypeScript knows, "Ah, user can be User or null".
const [user, setUser] = useState<User | null>(null);

The other place where TypeScript shines with Hooks is with userReducer, where you can take advantage of discriminated unions.

type AppState = {};
type Action =
| { type: "SET_ONE"; payload: string }
| { type: "SET_TWO"; payload: number };

export function reducer(state: AppState, action: Action): AppState {
switch (action.type) {
case "SET_ONE":
return {
...state,
one: action.payload // `payload` is string
};
case "SET_TWO":
return {
...state,
two: action.payload // `payload` is number
};
default:
return state;
}
}

### What should I use for React Props and State ?

In general, use what you want ( type alias / interface ) just be consistent, but personally, I recommend to use type aliases:

it’s shorter to write type Props = {}
your syntax is consistent ( you are not mixin interfaces with type aliases for possible type intersections )

// BAD
interface Props extends OwnProps, InjectedProps, StoreProps {}
type OwnProps = {...}
type StoreProps = {...}
// GOOD
type Props = OwnProps & InjectedProps & StoreProps
type OwnProps = {...}
type StoreProps = {...}

your public component Props/State implementation cannot be monkey patched and for that reason, consumer of your component should never need to leverage interface declaration merging. For extension there are clearly defined patterns like HOC and so on.

###
