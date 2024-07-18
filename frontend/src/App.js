import {BrowserRouter, Route, Routes} from "react-router-dom";
import HomePage from "./home/HomePage";
import {HelmetProvider} from "react-helmet-async";

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min';

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
