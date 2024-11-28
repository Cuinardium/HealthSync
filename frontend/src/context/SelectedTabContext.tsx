import {createContext, useContext} from "react";

interface SelectedTabContextType {
    selectedTab: string;
    setSelectedTab: (tab: string) => void;
}

export const SelectedTabContext = createContext<SelectedTabContextType | undefined>(undefined);

export const useSelectedTabContext = () => {
    const context = useContext(SelectedTabContext);
    if (!context) {
        throw new Error("useTabContext must be used within a TabProvider");
    }
    return context;
};