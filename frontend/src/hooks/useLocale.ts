import { useState, useCallback } from "react";

const useLocale = () => {
  const LOCAL_STORAGE_KEY = "i18nextLng";

  // Get the initial locale from localStorage
  const getInitialLocale = () => localStorage.getItem(LOCAL_STORAGE_KEY) || "";

  const [locale, setLocaleState] = useState<string>(getInitialLocale);

  const setLocale = useCallback((newLocale: string) => {
    localStorage.setItem(LOCAL_STORAGE_KEY, newLocale);
    setLocaleState(newLocale);
  }, []);

  const clearLocale = useCallback(() => {
    localStorage.removeItem(LOCAL_STORAGE_KEY);
    setLocaleState("");
  }, []);

  return {
    locale,
    setLocale,
    clearLocale,
  };
};

export default useLocale;
