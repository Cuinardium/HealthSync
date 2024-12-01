export function parseLocalDate(dateString: string): Date {
    const [year, month, day] = dateString.split("-");
    return new Date(Number(year), Number(month) - 1, Number(day));
}


export function formatDate(date: Date): string {
    return date.toLocaleDateString('en-CA');
}

export function isSameDay(date1: Date | undefined, date2: Date | undefined): boolean {
    if (!date1 || !date2) {
        return false;
    }
    return formatDate(date1) === formatDate(date2);
}
