import React, { ReactNode, useState } from "react";
import { SelectedTabContext } from "../context/SelectedTabContext";

interface TabProviderProps {
  children: ReactNode;
}

export const SelectedTabProvider: React.FC<TabProviderProps> = ({
  children,
}) => {
  const [selectedTab, setSelectedTab] = useState<string>("today");

  return (
    <SelectedTabContext.Provider value={{ selectedTab, setSelectedTab }}>
      {children}
    </SelectedTabContext.Provider>
  );
};
