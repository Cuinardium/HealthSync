import React from "react";
import { FaStar, FaStarHalfStroke } from "react-icons/fa6";

interface RatingProps {
    rating: number;
    count: number;
}

const Rating: React.FC<RatingProps> = ({ rating, count }) => {
    const maxStars = 5;

    const renderStars = () => {
        const stars = [];
        for (let i = 1; i <= maxStars; i++) {
            if (i <= Math.floor(rating)) {
                // Full Star
                stars.push(<FaStar size="20px" key={i} style={{color: "gold"}} />);
            } else if (i === Math.ceil(rating) && !Number.isInteger(rating)) {
                // Half Star
                stars.push(<FaStarHalfStroke size="20px" key={i} style={{color: "gold"}} />);
            } else {
                // Empty Star
                stars.push(<FaStar size="20px" key={i} style={{color: "lightgrey"}} />);
            }
        }
        return stars;
    };

    return (
        <div className="d-flex align-items-center">
            <div className="d-flex">{renderStars()}</div>
            <span className="ms-2 text-muted">({count})</span>
        </div>
    );
};

export default Rating;
