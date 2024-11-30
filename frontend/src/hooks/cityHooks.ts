import {useQuery} from "@tanstack/react-query";
import {City, CityQuery} from "../api/city/City";
import {getCities} from "../api/city/cityApi";
import {queryClient} from "../api/queryClient";


const STALE_TIME = 1000 * 60 * 30;


// ========== useCities ==========
export function useCities() {
  return useQuery<City[], Error>(
    {
      queryKey: ["cities"],
      queryFn: () => getCities(),
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}