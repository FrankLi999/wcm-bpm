import { CamelCaseToDashPipe } from "./camelCaseToDash.pipe";
describe("CamelCaseToDashPipe", () => {
  it("CamelCaseToDashPipe.transform", () => {
    const pipe = new CamelCaseToDashPipe();
    const expected = "this-is-transformed-camel-case";
    const result = pipe.transform("thisIsTransformedCamelCase");
    expect(result).toBe(expected);
  });
});
