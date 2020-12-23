import { HtmlToPlaintextPipe } from "./htmlToPlaintext.pipe";
describe("HtmlToPlaintextPipe", () => {
  it("HtmlToPlaintextPipe.transform", () => {
    const pipe = new HtmlToPlaintextPipe();
    const expected = "This is HtmlToPlaintext Pipe";
    const result = pipe.transform(
      "<div>This is </p>HtmlToPlaintext</p> Pipe</div>"
    );
    expect(result).toBe(expected);
  });
});
