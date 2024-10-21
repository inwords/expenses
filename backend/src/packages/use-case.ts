export interface UseCase<TInput, TOutput = void> {
    execute: (input: TInput) => TOutput | Promise<TOutput>;
}