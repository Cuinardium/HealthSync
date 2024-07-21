import React from 'react';
import { Alert, Container } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';


import 'bootstrap/dist/css/bootstrap.min.css';
import '../css/main.css';
import '../css/error.css';

const Error403 = () => {
    const { t } = useTranslation();


    return (
        <>
            <header>
                {/* Favicon cuando este */}
            </header>

            <Container className="errorContainer">
                <Alert variant="danger">
                    <h1>Error 403</h1>
                    <h4>{t('error.403')}</h4>
                </Alert>
            </Container>
        </>
    );
};

export default Error403;
