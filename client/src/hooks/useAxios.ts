import {useMemo} from 'react';
import axios, {type AxiosInstance} from "axios";

interface Exports {
    axiosClient: AxiosInstance;
}

const baseClient: AxiosInstance = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    timeout: 10_000,
});

export default function useAxios(): Exports {

    const axiosClient: AxiosInstance = useMemo(() => baseClient, []);
    return {axiosClient};
}