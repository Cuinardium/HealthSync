import React from "react";
import { Image } from "react-bootstrap";
import { Doctor, DoctorQuery } from "../../api/doctor/Doctor";
import { useDoctors } from "../../hooks/doctorHooks";
import Loader from "../Loader";

import doctorDefault from "../../img/doctorDefault.png";
import { Link } from "react-router-dom";

interface DoctorListProps {
  page?: number;
  pageSize?: number;
  onPageChange: (page: number) => void;
}

const DoctorList: React.FC<DoctorListProps> = ({
  page = 1,
  pageSize = 10,
  onPageChange,
}) => {
  const query: DoctorQuery = {
    page,
    pageSize,
  };

  const { data: doctorsPage, isLoading, isError, error } = useDoctors(query);

  if (isLoading) {
    // TODO
    return <Loader />;
  }

  if (isError) {
    // TODO
    return <div>Error fetching doctors: {error?.message}</div>;
  }

  if (!doctorsPage || doctorsPage.content.length === 0) {
    // TODO
    return <div>No doctors found</div>;
  }

  // TODO: Hacerlo bien
  return (
    <div>
      <h2>Doctors</h2>
      <ul>
        {doctorsPage.content.map((doctor: Doctor) => (
          <li key={doctor.id}>
            <div>
              <Image
                src={doctor.image ? doctor.image : doctorDefault}
                width="40"
                height="40"
                className="rounded-circle"
              />
            </div>
            <div>
              <strong>
                {doctor.firstName} {doctor.lastName}
              </strong>
            </div>
            <div>
              <strong>{doctor.specialty}</strong>
            </div>
            <div>
              <strong>{doctor.healthInsurances}</strong>
            </div>
            <div>
              <strong>City: {doctor.city}</strong>
            </div>
            <div>
              <strong>Address: {doctor.address}</strong>
            </div>
            {doctor.rating && (
              <div>
                <strong>Rating: {doctor.rating}</strong>
              </div>
            )}
            <Link to={`/detailed-doctor/${doctor.id}`}>View Details</Link>
          </li>
        ))}
      </ul>

      {/* Pagination Controls */}
      <div style={{ marginTop: "20px" }}>
        <button disabled={page === 1} onClick={() => onPageChange(page - 1)}>
          Previous
        </button>
        <span style={{ margin: "0 10px" }}>Page {page}</span>
        <button
          onClick={() => onPageChange(page + 1)}
          disabled={
            doctorsPage.currentPage === doctorsPage.totalPages
          }
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default DoctorList;
