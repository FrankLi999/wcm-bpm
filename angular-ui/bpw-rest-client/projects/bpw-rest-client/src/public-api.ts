/*
 * Public API Surface of rest-client
 */

export * from './lib/rest-client';
export { Client } from './lib/decorators/client';
export { Headers } from './lib/decorators/headers';
export { Map } from './lib/decorators/map';
export { Timeout } from './lib/decorators/timeout';
export { OnEmit } from './lib/decorators/on-emit';
export { Body, Header, Query, Path, PlainBody } from './lib/decorators/parameters';
export { MediaType, Produces } from './lib/decorators/produces';
export { Get, Post, Patch, Put, Delete, Head } from './lib/decorators/request-methods';

export * from './lib/rest-client.module';
export * from './lib/constants';