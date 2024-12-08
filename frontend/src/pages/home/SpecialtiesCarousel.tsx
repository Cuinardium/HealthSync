import React from "react";
import Carousel from "react-multi-carousel";
import "react-multi-carousel/lib/styles.css";
import {SpecialtyQuery} from "../../api/specialty/Specialty";
import {usePopularSpecialties} from "../../hooks/specialtyHooks";
import {Card} from "react-bootstrap";
import {useDoctorQueryContext} from "../../context/DoctorQueryContext";
import {useNavigate} from "react-router-dom";


const SpecialtiesCarousel: React.FC = () => {
    const { addSpecialty } = useDoctorQueryContext();
    const navigate = useNavigate();

    const handleSpecialtyClick = (specialty: string) => {
        addSpecialty(specialty.toUpperCase().replace(/\./g, "_"));
        navigate("/doctor-dashboard");
    };

    const query : SpecialtyQuery = {
        pageSize: 5,
    };

    const {
        data: specialties,
        isLoading,
        isSuccess,
        error,
    } = usePopularSpecialties(query);


    const responsive = {
        superLargeDesktop: {
            breakpoint: { max: 4000, min: 3000 },
            items: 5
        },
        desktop: {
            breakpoint: { max: 3000, min: 1024 },
            items: 4
        },
        tablet: {
            breakpoint: { max: 1024, min: 464 },
            items: 2
        },
        mobile: {
            breakpoint: { max: 464, min: 0 },
            items: 1
        }
    };

    if (isLoading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error loading specialties</div>;
    }

    if (!isSuccess || specialties.length === 0) {
        return null;
    }

    return (
        <Carousel
            swipeable={true}
            draggable={true}
            showDots={true}
            responsive={responsive}
            ssr={true} // means to render carousel on server-side.
            infinite={true}
            keyBoardControl={true}
        >
            {isSuccess && specialties.map((specialty) => (
                <Card className='carouselCard' key={specialty.code} onClick={() => handleSpecialtyClick(specialty.code)}>
                    <Card.Img className='carouselImg' variant="top" src={require(`../../img/carousel/${specialty.code.toLowerCase()}.jpg`)} />
                    <Card.ImgOverlay className='cardImageOverlay'>
                        <Card.Title>{specialty.name}</Card.Title>
                    </Card.ImgOverlay>
                </Card>
            ))}
        </Carousel>
    );
};

export default SpecialtiesCarousel;