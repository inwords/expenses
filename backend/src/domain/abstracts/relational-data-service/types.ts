export interface ITransaction {
  ctx?: unknown;
}

export interface ITransactionWithLock {
  ctx?: unknown;
  lock?: 'pessimistic_read' | 'pessimistic_write' | undefined;
  onLocked?: 'nowait' | 'skip_locked' | undefined;
}

export interface IQueryDetails {
  queryString: unknown;
  queryParameters: unknown;
}

export interface IRelationalDataService {
  initialize: () => Promise<void>;
  transaction: (<T, TEntityManager>(runInTransaction: (entityManager: TEntityManager) => Promise<T>) => Promise<T>) &
    (<T, TEntityManager, TIsolationLevel>(
      isolationLevel: TIsolationLevel,
      runInTransaction: (entityManager: TEntityManager) => Promise<T>,
    ) => Promise<T>);
  destroy: () => Promise<void>;
  flush: () => unknown;
}
