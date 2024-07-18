import {BrowserRouter, Route, Routes} from "react-router-dom";
import HomePage from "./home/HomePage";
import {HelmetProvider} from "react-helmet-async";

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min';
import DoctorProfile from "./user/DoctorProfile";
import PatientProfile from "./user/PatientProfile";
import Error403 from "./errors/403";
import Error404 from "./errors/404";
import Error500 from "./errors/500";

function App() {
    const helmetContext = {};

  return (
      <HelmetProvider context={helmetContext}>
          <BrowserRouter>
              <Routes>
                  <Route path="/" element={<HomePage />} />
                  <Route path="/doctor-profile" element={<DoctorProfile/>}/>
                  <Route path="/patient-profile" element={<PatientProfile/>}/>

                  {/* Error Pages */}
                  <Route path="*" element={<Error404/>} />
                  <Route path="/404" element={<Error404/>} />
                  <Route path='/500' element={<Error500/>}/>
                  <Route path='/403' element={<Error403/>}/>
              </Routes>
          </BrowserRouter>
      </HelmetProvider>
  );
}

export default App;
