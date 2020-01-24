import { required } from "@rxweb/reactive-form-validators";

export class Course {
    @required() courseName: string;
}
