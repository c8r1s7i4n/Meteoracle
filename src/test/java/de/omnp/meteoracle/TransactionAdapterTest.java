/*
    Meteoracle: Industrial Supply Chain Interoperability Layer
    Copyright (C) 2026  Christian Beissmann

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

    Original source: https://github.com/c8r1s7i4n/Meteoracle
    For contact and support, visit: https://omnipons.de
*/

package de.omnp.meteoracle;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.omnp.meteoracle.domain.vda4994.JLocation;
import de.omnp.meteoracle.domain.vda4994.Scan;
import de.omnp.meteoracle.infrastructure.ScannerMapper;
import de.omnp.meteoracle.infrastructure.spi.IotaMetadata;
import de.omnp.meteoracle.infrastructure.spi.TransactionAdapter;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@ExtendWith(MockitoExtension.class)
class TransactionAdapterTest {

    private MockWebServer mockWebServer;
    private TransactionAdapter adapter;

    @Mock
    private IotaMetadata metadata;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // 2. Get the ACTUAL local URL from the server (e.g., http://localhost:54321)
        String localUrl = mockWebServer.url("/").toString();

        // Inject the local mock server URL into your metadata mock
        this.metadata = new IotaMetadata(localUrl, "iotaprivkey1qz2kv3fjtnwfk9n838az04mq5faakajgja7hggxrgas0ke5pd7fe7u9qwn6");
        // Mock other metadata fields (wallet, etc.) if the private methods use them
        
        adapter = new TransactionAdapter(Mappers.getMapper(ScannerMapper.class), metadata);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testSendTransaction_Success() throws Exception {

        Scan dto = new Scan("Package 01x333","EAN-13",
        "3700123300014",
        "2026-03-01T12:00:00Z",
        "myAndroidScanner",
        "scanTransaction",
        new JLocation(45, 46),
        null
       );

       
        // 1. Queue Response for 'prepareTransaction' (The first POST call)
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"result\": {\"txBytes\": \"dGhpcyBpcyBhIGZha2UgdHJhbnNhY3Rpb24=\"}}")
                .addHeader("Content-Type", "application/json"));

        // 2. Queue Response for 'executeTransaction' (The second POST call)
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        // 3. Act: Call the PUBLIC method
        boolean result = adapter.sendTransaction(dto);

        // 4. Assert
        assertTrue(result);
        
        // Bonus: Verify the private 'prepareTransaction' logic sent the right JSON
        RecordedRequest request1 = mockWebServer.takeRequest();
        assertTrue(request1.getBody().readUtf8().contains("unsafe_moveCall"));
    }
}
