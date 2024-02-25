import logging
import os
import smtplib
from email.mime.text import MIMEText

import config


class EmailService:
    def __init__(self):
        logging.basicConfig(level=logging.INFO)
        self.logger = logging.getLogger(__name__)

        self.smtpObj = smtplib.SMTP('smtp.gmail.com', 587)

    def test_send(self) -> None:
        try:
            self.smtpObj.starttls()
            self.smtpObj.login(os.environ['EMAIL_LOGIN'], os.environ['EMAIL_PASSWORD'])

            self.smtpObj.sendmail(os.environ['EMAIL_LOGIN'], os.environ['EMAIL_TEST'], config.TEST_MESSAGE.as_string())

            self.smtpObj.quit()

            self.logger.info(f"(Send test mail) Successful sent test message to {os.environ['EMAIL_TEST']}")
        except Exception as e:
            self.logger.error(f"(Send test mail) Error: {e}")
            raise

    def send_link(self, code: str, to_email: str):
        try:
            self.smtpObj.starttls()
            self.smtpObj.login(os.environ['EMAIL_LOGIN'], os.environ['EMAIL_PASSWORD'])

            msg = MIMEText(f'<a href={os.environ["VERIFY_LINK"] + code}>Click to verify</a>', 'html')
            msg['From'] = os.environ['EMAIL_LOGIN']
            msg['To'] = to_email
            msg['Subject'] = 'PmC pYtHoN VeRiFy'

            self.smtpObj.sendmail(os.environ['EMAIL_LOGIN'], to_email, msg.as_string())

            self.smtpObj.quit()

            self.logger.info(f"(Send link) Successful sent link to {to_email}")
        except Exception as e:
            self.logger.error(f"(Send link) Error: {e}")
            raise
