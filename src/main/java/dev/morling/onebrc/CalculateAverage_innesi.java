/*
 *  Copyright 2023 The original authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package dev.morling.onebrc;

import static java.util.stream.Collectors.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * i5-5200U / 16GB / SATA SSD
 * Baseline : 6m8,445s
 * Naive : 5m59,698s
 */

public class CalculateAverage_innesi {

    private static final String FILE = "./measurements.txt";

    public static void main(String[] args) throws IOException {

        BufferedReader buffReader = new BufferedReader(new StringReader(FILE));
        Map<String, DoubleSummaryStatistics> mesures = new TreeMap<>();

        String line = null;
        while ((line = buffReader.readLine()) != null) {
            String ct[] = line.split(";");
            if (mesures.containsKey(ct[0])) {
                mesures.get(ct[0]).accept(Double.parseDouble(ct[1]));
            } else {
                DoubleSummaryStatistics dss = new DoubleSummaryStatistics();
                dss.accept(Double.parseDouble(ct[1])); 
                mesures.put(ct[0], dss);
            }
        }

        System.out.println(mesures);
    }
}
