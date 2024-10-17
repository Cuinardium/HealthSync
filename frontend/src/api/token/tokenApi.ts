import { axios } from "../axios";

const VERIFICATION_TOKEN_ENDPOINT = "/tokens/verification";
const EMAIL_CONTENT_TYPE = "application/vnd.email.v1+json";

// ================ verification ================

export async function resendVerificationToken(email: string): Promise<void> {
    const emailBody = {
        email: email,
    }

    await axios.post<void>(VERIFICATION_TOKEN_ENDPOINT, emailBody, {
        headers: {
            "Content-Type": EMAIL_CONTENT_TYPE,
        },
    });
}

export async function verifyUser(token: string, email: string): Promise<void> {
    const emailBody = {
        email: email,
    }

    await axios.patch<void>(`${VERIFICATION_TOKEN_ENDPOINT}/${token}`, emailBody, {
        headers: {
            "Content-Type": EMAIL_CONTENT_TYPE,
        },
    });
}
