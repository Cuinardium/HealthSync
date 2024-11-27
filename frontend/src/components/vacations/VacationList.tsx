import React from "react";
import { useVacations, useDeleteVacation } from "../../hooks/vacationHooks";
import VacationCard from "./VacationCard";
import { Alert, Button, Spinner, Stack } from "react-bootstrap";
import { useTranslation } from "react-i18next";
import VacationCardPlaceholder from "./VacationCardPlaceholder";

interface VacationPageProps {
  doctorId: string;
  pageSize: number;
}

const VacationList: React.FC<VacationPageProps> = ({ doctorId, pageSize }) => {
  const {
    data: vacations,
    isLoading,
    error,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
  } = useVacations(doctorId, pageSize);

  const { t } = useTranslation();

  const deleteVacationMutation = useDeleteVacation(doctorId);

  const handleDeleteVacation = (vacationId: string) => {
    deleteVacationMutation.mutate(vacationId);
  };

  if (isLoading) {
    return <Stack direction="vertical" gap={3}>
      {[...Array(pageSize)].map((_, index) => (
        <VacationCardPlaceholder key={index} />
      ))}
    </Stack>;
  }

  if (error) {
    return (
      <div
        className={"text-center d-flex flex-row justify-content-center mt-5"}
      >
        <Alert variant="danger">{t("vacation.errorLoading")}</Alert>
      </div>
    );
  }

  if (
    !vacations ||
    vacations.pages.length === 0 ||
    vacations.pages[0].content.length === 0
  ) {
    return (
      <div
        className={"text-center d-flex flex-row justify-content-center mt-5"}
      >
        <Alert variant="info">{t("vacation.noVacations")}</Alert>
      </div>
    );
  }
  // TODO
  return (
    <div>
      {/* List of vacations */}
      <Stack direction="vertical" gap={3}>
        {vacations.pages.map((page, index) => (
          <React.Fragment key={index}>
            {page.content.map((vacation) => (
              <VacationCard
                key={vacation.id}
                vacation={vacation}
                onDelete={handleDeleteVacation}
              />
            ))}
          </React.Fragment>
        ))}
      </Stack>

      {/* Load more */}
      <div style={{ marginTop: "20px", textAlign: "center" }}>
        {hasNextPage && (
          <Button
            variant="outline-primary"
            onClick={() => fetchNextPage()}
            disabled={isFetchingNextPage}
          >
            {isFetchingNextPage ? (
              <>
                <Spinner
                  as="span"
                  animation="border"
                  size="sm"
                  role="status"
                  aria-hidden="true"
                />{" "}
                {t("vacation.loadingMore")}
              </>
            ) : (
              t("vacation.loadMore")
            )}
          </Button>
        )}
      </div>
    </div>
  );
};

export default VacationList;
