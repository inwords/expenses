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