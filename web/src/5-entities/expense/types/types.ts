export interface CreateExpense {
    description: string;
    userWhoPaidId: number;
    currencyId: number;
    eventId: number;
    splitInformation: Array<SplitInfo>;
}

export interface SplitInfo {
    userId: string;
    amount: number;
}


export interface Expense {
    id: string;
    description: string;
    userWhoPaidId: number;
    currencyId: number;
    eventId: number;
    splitInformation: Array<SplitInfo>;
    createdAt: string;
}

export type Tabs = 0 | 1 | 2 | 3 | 4;