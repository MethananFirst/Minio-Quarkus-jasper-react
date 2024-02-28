import { useEffect, useState } from "react";
import Layout from "./Layout.jsx";

const ListFile = ({ bucket }) => {
    const [files, setFiles] = useState([]);

    useEffect(() => {
        const getBucketFiles = async () => {
            try {
                const res = await fetch(`http://localhost:8080/minio/${bucket}/listAllFile`, {
                    method: "GET",
                });

                const data = await res.json();
                setFiles(data);

                if (!res.ok) {
                    throw new Error(`Failed to fetch data: ${res.status}`);
                }

            } catch (error) {
                console.error("Error fetching data:", error.message);
            }
        };

        getBucketFiles();
    }, [bucket]);

    return (
        <div>
            <Layout/>
            <h1>File name</h1>
            {files.map((file) => (
                <p>${file}</p>
            ))}
        </div>
    );
};

export default ListFile;
