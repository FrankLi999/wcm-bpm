import { GetByIdPipe } from "./getById.pipe";
describe("GetByIdPipe", () => {
  it("GetByIdPipe.transform", () => {
    const pipe = new GetByIdPipe();
    const expected = "This is HtmlToPlaintext Pipe";
    const result = pipe.transform(
      [
        { id: 1, key: "value" },
        { id: 2, key1: "value1" },
        { id: 2, key1: "value2" },
      ],
      2,
      "key1"
    );
    expect(result).toBe("value1");
  });

  it("get Undefined", () => {
    const pipe = new GetByIdPipe();
    const expected = "This is HtmlToPlaintext Pipe";
    const result = pipe.transform(
      [
        { id: 1, key: "value" },
        { id: 2, key2: "value1" },
        { id: 2, key1: "value2" },
      ],
      2,
      "key1"
    );
    expect(result).toBeUndefined();
  });
});
