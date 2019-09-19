import { HasName } from './HasName';
import { RestNode } from './RestNode';
import { WcmLibrary } from './WcmLibrary';
export interface WcmWorkspace extends HasName {
    libraries: WcmLibrary[];
}