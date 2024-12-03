import React, { useEffect } from "react";
import { IndicationQuery } from "../../api/indication/Indication";
import { useIndications } from "../../hooks/indicationHooks";
import { Alert, Card, Spinner, Stack } from "react-bootstrap";
import { useTranslation } from "react-i18next";
import IndicationCard from "./IndicationCard";

import { useAuth } from "../../context/AuthContext";
import IndicationForm from "./IndicationForm";

interface IndicationListProps {
  appointmentId: string;
  pageSize?: number;
}

const IndicationList: React.FC<IndicationListProps> = ({
  appointmentId,
  pageSize = 10,
}) => {
  const query: IndicationQuery = {
    pageSize,
  };

  const { t } = useTranslation();

  const { id, loading } = useAuth();

  const chatRef = React.useRef<HTMLDivElement>(null);

  const {
    data: indications,
    isLoading,
    isSuccess,
      isRefetching,
    error,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
  } = useIndications(appointmentId, query);

  const scrollToBottom = () => {
    if (chatRef.current) {
      chatRef.current.scrollTop = chatRef.current.scrollHeight;
    }
  };

  useEffect(() => {
    if (isSuccess || isRefetching) {
      scrollToBottom();
    }
  }, [chatRef, isSuccess, isRefetching]);

  if (error) {
    return <div>Error fetching Indications: {error?.message}</div>;
  }

  return (
    <Card>
      <Card.Body>
        <div
          ref={chatRef}
          style={{ maxHeight: "700px", minHeight: "700px", overflowY: "auto" }}
        >
          {/* Load More */}
          {hasNextPage && (
            <div className="text-center mt-3">
              <button
                className="btn btn-primary"
                onClick={() => fetchNextPage()}
                disabled={isFetchingNextPage}
              >
                {isFetchingNextPage ? (
                  <Spinner
                    as="span"
                    animation="border"
                    size="sm"
                    role="status"
                    aria-hidden="true"
                  />
                ) : (
                  t("appointment.moreIndications")
                )}
              </button>
            </div>
          )}

          <Stack
            direction="vertical"
            gap={3}
            style={{ minHeight: "700px" }}
            className="d-flex flex-column-reverse"
          >
            {(isLoading || loading) && (
              <div className="text-center mt-5">
                <Spinner animation="border" variant="primary" />
              </div>
            )}
            {!isLoading &&
              (!indications ||
                indications.pages.length === 0 ||
                indications.pages[0].content.length === 0) && (
                <div
                  className={
                    "text-center d-flex flex-row justify-content-center mt-5"
                  }
                >
                  <Alert variant="info">{t("appointment.noIndications")}</Alert>
                </div>
              )}
            {indications?.pages
              .flatMap((page) => page.content)
              .slice()
              .map((indication) => (
                <div
                  key={indication.id}
                  className={`d-flex ${
                    indication.creatorId === id
                      ? "justify-content-end"
                      : "justify-content-start"
                  }`}
                >
                  <IndicationCard
                    indication={indication}
                    isCreator={indication.creatorId === id}
                  />
                </div>
              ))}
          </Stack>
        </div>

        <hr className="mt-5" />

        <IndicationForm
          appointmentId={appointmentId}
          onSuccess={scrollToBottom}
        />
      </Card.Body>
    </Card>
  );
};

export default IndicationList;
