import React from "react";
import { FaStar } from "react-icons/fa6";
import { useTranslation } from "react-i18next";

interface RatingProps {
  onClick: (rating?: number) => void;
  initialRating?: number;
  showClear?: boolean;
}

const ClickableRating: React.FC<RatingProps> = ({ onClick, initialRating, showClear = true }) => {
  const maxStars = 5;
  const { t } = useTranslation();

  const handleClick = (rating: number) => {
    const newRating = rating === 0 ? undefined : rating;
    onClick(newRating);
  };

  const rating = initialRating || 0;

  const renderStars = () => {
    const stars = [];
    for (let i = 1; i <= maxStars; i++) {
      if (i <= Math.floor(rating)) {
        // Full Star
        stars.push(
          <FaStar
            size="20px"
            key={i}
            style={{ color: "gold", cursor: "pointer" }}
            onClick={() => handleClick(i)}
          />,
        );
      } else {
        // Empty Star
        stars.push(
          <FaStar
            size="20px"
            key={i}
            style={{ color: "lightgrey", cursor: "pointer" }}
            onClick={() => handleClick(i)}
          />,
        );
      }
    }
    return stars;
  };

  return (
    <div className="d-flex align-items-center">
      <div className="d-flex">{renderStars()} </div>
      {rating > 0 && showClear && (
        <a
          className="ms-2"
          href="/"
          onClick={(e) => {
            e.preventDefault();
            e.stopPropagation();
            handleClick(0);
          }}
        >
          {t("filters.clear")}
        </a>
      )}
    </div>
  );
};

export default ClickableRating;
