import { useTranslation } from 'react-i18next';


const Header = () => {
  const { t } = useTranslation();

  return (
    <h1>{t('nav.myAppointments')}</h1>
  );
}

export default Header;