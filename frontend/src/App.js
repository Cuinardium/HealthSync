import {BrowserRouter, Route, Routes} from "react-router-dom";
import HomePage from "./home/HomePage";
import {HelmetProvider} from "react-helmet-async";

function App() {
    const helmetContext = {};

  return (
      <HelmetProvider context={helmetContext}>
          <BrowserRouter>
              <Routes>
                  <Route path="/" element={<HomePage />} />
              </Routes>
          </BrowserRouter>
      </HelmetProvider>
  );
}

export default App;
