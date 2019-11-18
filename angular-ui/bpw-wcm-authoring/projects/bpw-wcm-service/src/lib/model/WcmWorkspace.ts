import { HasName } from './HasName';
import { WcmLibrary } from './WcmLibrary';
export interface WcmWorkspace extends HasName {
    libraries: WcmLibrary[];
}