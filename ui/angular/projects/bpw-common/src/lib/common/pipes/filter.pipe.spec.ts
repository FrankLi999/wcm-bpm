import { FilterPipe } from "./filter.pipe";
describe("FilterPipe", () => {
  it("empty searchText", () => {
    const pipe = new FilterPipe();
    const result = pipe.transform(["str1", "str2"], "");
    expect(result.length).toBe(2);
  });
  it("filter by string array", () => {
    const pipe = new FilterPipe();
    const result = pipe.transform(
      [{ key1: "str1" }, { key1: "str2" }, { key1: "s-tr3" }],
      "str"
    );
    expect(result.length).toBe(2);
  });
  it("not case sensitive", () => {
    const pipe = new FilterPipe();
    const result = pipe.transform(
      [{ key1: "Str1" }, { key1: "sTr2" }, { key1: "s-tr3" }],
      "stR"
    );
    expect(result.length).toBe(2);
  });

  it("array values", () => {
    const pipe = new FilterPipe();
    const result = pipe.transform(
      [{ key1: ["other string", "Str1"] }, { key1: "sTr2" }, { key1: "s-tr3" }],
      "stR"
    );
    expect(result.length).toBe(2);
  });

  it("nested values", () => {
    const pipe = new FilterPipe();
    const result = pipe.transform(
      [
        {
          key1: {
            subkey1: "other string",
            subkey2: "Str1",
          },
        },
        { key1: "sTr2" },
        { key1: "s-tr3" },
      ],
      "stR"
    );
    expect(result.length).toBe(2);
  });
});
