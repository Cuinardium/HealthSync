import React from 'react';
import {Helmet} from "react-helmet-async";
import Image from 'react-bootstrap/Image';


import {useTranslation} from 'react-i18next';

import circle1 from '../../img/circle1.svg';
import circle2 from '../../img/circle2.svg';
import circle3 from '../../img/circle3.svg';
import homeDoctorImg from '../../img/homeDoctor.svg';

import 'bootstrap/dist/css/bootstrap.css';
import '../../css/main.css';
import '../../css/home.css';
import { Link } from 'react-router-dom';

const homeUrl = '/';

const HomePage = () => {
    const {t} = useTranslation();

    const title = t('home.home');
    const welcome1 = t('home.welcome1');
    const welcome2 = t('home.welcome2');
    const circleText1 = t('home.circle1');
    const circleText2 = t('home.circle2');
    const circleText3 = t('home.circle3');
    const description1 = t('home.description1');
    const description2 = t('home.description2');
    const description3 = t('home.description3');
    const altHomeDoctorImg = t('home.alt.homeDoctorImg');
    const altCircle1 = t('home.alt.circle1');
    const altCircle2 = t('home.alt.circle2');
    const altCircle3 = t('home.alt.circle3');

    return (
        <div>
            <Helmet>
                <title>{title}</title>
            </Helmet>

            {/*<Favicon />*/}

            <div className="generalPadding welcome">
                <div className="welcome1Container">
                    <div className="sloganSmall">{welcome1}</div>
                    <h1 className="sloganBig"><span className="text-gradient">{welcome2}</span></h1>
                </div>
                <div className="profile">
                    <Image src={homeDoctorImg} alt={altHomeDoctorImg} className="profile-img"/>
                </div>

            </div>


            {/*<div className="categories generalPadding">*/}
            {/*    <h2>{categories}</h2>*/}
            {/*    <div className="carousel slide" id="recipeCarousel" data-bs-ride="carousel">*/}
            {/*        <a className="carouselNavButton" href="#recipeCarousel" role="button" data-bs-slide="prev">*/}
            {/*            <i className="fa-solid fa-angle-left"></i>*/}
            {/*        </a>*/}
            {/*        <div className="carousel-inner" role="listbox">*/}
            {/*            /!* Iterate through categories to generate carousel items *!/*/}
            {/*            {categories.map((specialty, index) => {*/}
            {/*                // Define these variables dynamically based on your API response*/}
            {/*                const specialistImg = '/path/to/specialist/image';  // Replace with actual image path*/}
            {/*                const specialtyName = 'Specialty Name';  // Replace with actual specialty name*/}
            {/*                const altSpecialistImg = `Image of ${specialtyName.toLowerCase()}`;*/}
            {/*                return (*/}
            {/*                    <div className={`carousel-item ${index === 0 ? 'active' : ''}`} key={specialty.id}>*/}
            {/*                        <div className="card">*/}
            {/*                            <div className="card-img">*/}
            {/*                                <img src={specialistImg} className="img-fluid" alt={altSpecialistImg} />*/}
            {/*                            </div>*/}
            {/*                            <div className="card-img-overlay">{specialtyName.toLowerCase()}</div>*/}
            {/*                            <a href={`${doctorDashboardFilteredUrl}${specialty.ordinal}`} className="stretched-link"></a>*/}
            {/*                        </div>*/}
            {/*                    </div>*/}
            {/*                );*/}
            {/*            })}*/}
            {/*        </div>*/}
            {/*        <a className="carouselNavButton" href="#recipeCarousel" role="button" data-bs-slide="next">*/}
            {/*            <i className="fa-solid fa-angle-right"></i>*/}
            {/*        </a>*/}
            {/*    </div>*/}
            {/*</div>*/}

            <section className="about generalPadding border-top">
                <div className="aboutCircleContainer">
                    <Image className="circles" src={circle1} alt={altCircle1}/>
                    <h3>{circleText1}</h3>
                    <p>{description1}</p>
                </div>
                <div className="aboutCircleContainer">
                    <Image className="circles" src={circle2} alt={altCircle2}/>
                    <h3>{circleText2}</h3>
                    <p>{description2}</p>
                </div>
                <div className="aboutCircleContainer">
                    <Image className="circles" src={circle3} alt={altCircle3}/>
                    <h3>{circleText3}</h3>
                    <p>{description3}</p>
                </div>
            </section>

            <footer className="foot border-top horizontalPadding">
                <Link className="navbar-brand" to={homeUrl}>
                    <div className="health title">Health</div>
                    <div className="sync title">Sync</div>
                </Link>
                <div>
                    <span className="text-body-secondary">&copy; 2023 HealthSync, Inc</span>
                </div>
            </footer>
        </div>
    );
};

export default HomePage;
