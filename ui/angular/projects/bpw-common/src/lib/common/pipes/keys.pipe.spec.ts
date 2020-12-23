import { KeysPipe } from "./keys.pipe";
var chai = require("chai");

describe("KeysPipe", () => {
  it("KeysPipe.transform", () => {
    const pipe = new KeysPipe();
    const value = {
      key1: "value1",
      key2: "value2",
    };
    const keys = pipe.transform(value);
    expect(keys.length).toBe(2);
    expect(keys[0].key).toBe("key1");
    expect(keys[0].value).toBe("value1");
    expect(keys[1].key).toBe("key2");
    expect(keys[1].value).toBe("value2");

    chai.expect(keys).to.have.lengthOf(2);
    chai.expect(keys[0].key).to.equal("key1");
    chai.expect(keys[0].value).to.equal("value1");
    chai.expect(keys[1].key).to.equal("key2");
    chai.expect(keys[1].value).to.equal("value2");
  });
});
