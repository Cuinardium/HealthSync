export type Locale = "en" | "es";

export const LOCALES: Locale[] = ["en", "es"];

export function parseLocale(locale: string): Locale {
  let parsedLocale = locale.split("_")[0] as Locale;
  if (!LOCALES.includes(parsedLocale)) {
    parsedLocale = "en";
  }
  return parsedLocale;
}
