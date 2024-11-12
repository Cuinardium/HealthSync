import React from 'react';
import { Alert, Container } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';


import 'bootstrap/dist/css/bootstrap.min.css';
import '../../css/main.css';
import '../../css/error.css';

const Error404 = () => {
    const { t } = useTranslation();


    return (
        <>
            <header>
                {/* Favicon cuando este */}
            </header>

            <Container className="errorContainer">
                <Alert variant="danger">
                    <h1>Error 404</h1>
                    <h4>{t('error.404')}</h4>
                </Alert>
            </Container>
        </>
    );
};

export default Error404;