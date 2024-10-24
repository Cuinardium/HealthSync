import { useNavigate, useParams } from "react-router-dom";
import { Image } from "react-bootstrap";
import { useDoctor } from "../../hooks/doctorHooks";

import doctorDefault from "../../img/doctorDefault.png";

export const DoctorDetails: React.FC = () => {
  const { id } = useParams<{ id: string }>(); // get the doctor id from the URL
  const navigate = useNavigate();

  const { data: doctor, isLoading, isError, error } = useDoctor(id as string);

  if (!id || isNaN(+Number(id)) || Number(id) < 0) {
    navigate("/404");
    return null;
  }

  if (isLoading) {
    // TODO
    return <div>Loading...</div>;
  }

  if (isError) {
    //TODO
    return <div>{error?.message}</div>;
  }

  if (!doctor) {
    navigate("/404");
    return null;
  }

  return (
    <div className="doctor-details">
      <h2>
        {doctor.firstName} {doctor.lastName}
      </h2>
      <Image
        src={doctor.image || doctorDefault}
        alt={`${doctor.firstName} ${doctor.lastName}`}
        width={100}
        height={100}
        className="rounded-circle"
      />
      <p>
        <strong>Specialty:</strong> {doctor.specialty}
      </p>
      <p>
        <strong>Health Insurances:</strong> {doctor.healthInsurances.join(", ")}
      </p>
      <p>
        <strong>City:</strong> {doctor.city}
      </p>
      <p>
        <strong>Address:</strong> {doctor.address}
      </p>
      {doctor.rating && (
        <p>
          <strong>Rating:</strong> {doctor.rating} / 5
        </p>
      )}
    </div>
  );
};
