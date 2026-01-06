export type Result<T, E> = {result: 'success'; value: T} | {result: 'error'; error: E};

export const success = <T, E>(value: T): Result<T, E> => ({result: 'success', value});

export const error = <T, E>(error: E): Result<T, E> => ({result: 'error', error});

export const isSuccess = <T, E>(result: Result<T, E>): result is {result: 'success'; value: T} =>
  result.result === 'success';

export const isError = <T, E>(result: Result<T, E>): result is {result: 'error'; error: E} => result.result === 'error';
