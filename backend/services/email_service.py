import logging
import os
import smtplib

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
