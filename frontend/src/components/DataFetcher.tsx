import * as React from 'react';
import { useEffect, useState } from 'react';
import { CircularProgress, Container, Grid } from '@mui/material';
import { NonIdealState } from 'components/NonIdealState';
import { usePromise } from 'hooks';

/**
 * @param data This is a render-prop which you use to only render your content
 *  when data has come into effect
 * @param setDependencyList This is a callback which you use to set the list of
 *  dependencies for the useEffect hook.
 *  Using this will allow you to re-fetch the data if you have a dependency on a submit for example.
 */
type Child<TData> = (
  // eslint-disable-next-line no-unused-vars
  data: TData,
  // eslint-disable-next-line no-unused-vars
  setDependencyList: (dependencies: any) => void,
  // eslint-disable-next-line no-unused-vars
  setData: (value: (current: TData | undefined) => TData) => void,
) => React.ReactNode;

interface IProps<TData> {
  apiMethod: () => Promise<TData>;
  children: Child<TData>;
  errorText: string;
  errorIndicator?: React.ReactNode;
  notFoundIndicator?: React.ReactNode;
  loadingIndicator?: React.ReactNode;
}

export const DataFetcher = <TData extends {}>({
  apiMethod,
  children,
  errorText,
  errorIndicator,
  notFoundIndicator,
  loadingIndicator,
}: IProps<TData>): JSX.Element => {
  const {
    loading,
    data,
    error,
    trigger,
    errorCode,
    setData,
  } = usePromise(apiMethod);
  const [dependencyList, setDependencyList] = useState(undefined);

  useEffect(() => {
    trigger();
  }, [dependencyList, trigger]);

  const renderError = (): any => {
    if (errorIndicator && errorCode !== 404 && notFoundIndicator) {
      return errorIndicator;
    } if (errorCode === 404 && notFoundIndicator) {
      return notFoundIndicator;
    }

    return errorCode !== 401 ? (
      <NonIdealState title={errorText} />
    ) : (
      <CircularProgress />
    );
  };

  return (
    <>
      {loading
        && (loadingIndicator || (
          <Container>
            <Grid container justifyContent="center">
              <Grid item>
                <CircularProgress size={64} />
              </Grid>
            </Grid>
          </Container>
        ))}
      {error && renderError()}
      {data !== undefined
        && children(
          data,
          (dependencies: any) => setDependencyList(dependencies),
          setData,
        )}
    </>
  );
};
