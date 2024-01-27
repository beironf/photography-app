import React, { useState, useCallback, useEffect } from 'react';
import { AxiosPromise, AxiosResponse } from 'axios';

const isAxiosResponse = (
  variableToCheck: any,
): variableToCheck is AxiosResponse =>
  (variableToCheck as AxiosResponse)?.status !== undefined &&
  (variableToCheck as AxiosResponse)?.request !== undefined;

type UsePromiseResponse<TData> = {
  data: TData;
  loading: boolean;
  error: boolean;
  trigger: () => Promise<void>;
  errorCode?: number;
  errorMessage?: string;
  reset: () => void;
  setData: (_: React.SetStateAction<TData>) => void;
};

export function usePromise<TData>(
  promise: () => Promise<TData> | AxiosPromise<TData>,
  initLoading?: boolean,
): UsePromiseResponse<TData> {
  const [data, setData] = useState<TData | undefined>(undefined);
  const [loading, setLoading] = useState(initLoading ?? false);
  const [error, setError] = useState(false);
  const [errorCode, setErrorCode] = useState<number | undefined>(undefined);
  const [errorMessage, setErrorMessage] = useState<string | undefined>(
    undefined,
  );
  const [shouldFetch, setShouldFetch] = useState(false);

  const resetError = (): void => {
    setErrorCode(undefined);
    setError(false);
    setErrorMessage(undefined);
  };

  const trigger = useCallback(async () => {
    setLoading(true);
    setError(false);
    setData(undefined);

    setShouldFetch(true);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [promise]);

  useEffect(() => {
    let didCancel = false;
    if (shouldFetch) {
      // eslint-disable-next-line func-names
      const anyNameFunction = async function (): Promise<void> {
        try {
          const result = await promise();
          if (didCancel) {
            // Don't try to update state on unmounted component
          } else if (isAxiosResponse(result)) {
            resetError();
            setData(result.data);
          } else {
            resetError();
            setData(result);
          }
        } catch (requestError) {
          if (!didCancel) {
            setErrorCode(requestError?.response?.status);
            setErrorMessage(
              requestError?.response?.data?.message ??
                requestError?.message ??
                'Unhandled error occurred',
            );
            setError(true);
          }
        }
        setShouldFetch(false);
        setLoading(false);
      };

      anyNameFunction();
    }
    return () => {
      didCancel = true;
    };
  }, [promise, shouldFetch]);

  return {
    data,
    loading,
    error,
    trigger,
    errorCode,
    errorMessage,
    reset: () => setData(undefined),
    setData,
  };
}
