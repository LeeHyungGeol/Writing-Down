package sample.cafekiosk.spring.api.service.mail;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

	@Mock
	private MailSendClient mailSendClient;

	@Mock
	private MailSendHistoryRepository mailSendHistoryRepository;

	@InjectMocks
	private MailService mailService;

	@Test
	@DisplayName("메일 전송 테스트")
	void sendMail() {
		// given
//		Mockito.when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
//			.thenReturn(true);

		BDDMockito.given(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
			.willReturn(true);

//		doReturn(true)
//			.when(mailSendClient)
//			.sendEmail(anyString(), anyString(), anyString(), anyString());

		// when
		boolean result = mailService.sendMail("from", "to", "subject", "content");

		// then
		assertThat(result).isTrue();

		Mockito.verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
	}
}
