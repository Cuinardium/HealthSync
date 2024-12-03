export function parseLocalDate(dateString: string): Date {
  const [year, month, day] = dateString.split("-");
  return new Date(Number(year), Number(month) - 1, Number(day));
}

export function formatDate(date: Date): string {
  return date.toLocaleDateString("en-CA");
}

export function formatDatePretty(date: Date): string {
  return date.toLocaleDateString("en-GB");
}

export function formatDatePrettyLong(date: Date, locale: string) {
  try {
    return date.toLocaleDateString(locale, {
      weekday: "long",
      year: "numeric",
      month: "long",
      day: "numeric",
    });
  } catch (e) {
    return formatDatePretty(date);
  }
}

export function isSameDay(
  date1: Date | undefined,
  date2: Date | undefined,
): boolean {
  if (!date1 || !date2) {
    return false;
  }
  return formatDate(date1) === formatDate(date2);
}
