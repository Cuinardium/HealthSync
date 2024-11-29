export function parseLocalDate(dateString: string): Date {
    const [year, month, day] = dateString.split("-");
    return new Date(Number(year), Number(month) - 1, Number(day));
}


export function formatDate(date: Date): string {
    return date.toLocaleDateString('en-CA');
}
