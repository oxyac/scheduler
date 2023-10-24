package com.oxyac.horaire.data.seeder;

import com.oxyac.horaire.data.entity.Schedule;
import com.oxyac.horaire.data.repo.ScheduleRepository;
import com.oxyac.horaire.telegram.ScheduleType;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@EnableAsync
public class ASEMScheduleSeeder {

    @Autowired
    private ScheduleRepository repository;

    public ASEMScheduleSeeder() {
    }

    public static Set<String> findLinks(String url) throws IOException {

        Set<String> links = new HashSet<>();

        Document doc = Jsoup.connect(url)
                .data("query", "Java")
                .userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(3000)
                .get();

        Elements elements = doc.select("a[href]");
        for (Element element : elements) {
            links.add(element.attr("href"));
        }

        return links;

    }

    @Scheduled(fixedDelay = 24, timeUnit = TimeUnit.HOURS)
    @Async
    public void seedDB() {
        log.info("SEEDING");
        this.repository.deleteAll();
        fillByLink("http://orar.ase.md/frecventa_redusa");
        fillByLink("http://orar.ase.md/orar_zi");
        log.info("SEEDING DONE");
    }

    private void fillByLink(String sourceURI) {
        try {
            for (String link : findLinks(sourceURI)) {
                String encodedUrl = link.replaceAll(" ", "%20");
                URI uri;
                try {
                    uri = new URI(encodedUrl);
                } catch (URISyntaxException e) {
                    log.error(e.getMessage());
                    continue;
                }
                String domain = uri.getHost();
                String path = uri.getPath();
                List<String> paths = getPath(path);
                if (!Objects.equals(paths.get(0), "documents")) {
                    continue;
                }
                if (!Objects.equals(domain, "orar.ase.md")) {
                    continue;
                }
                if (paths.get(2).equals("2014-2021")) {
                    paths.remove(2);
                }
                Schedule schedule = new Schedule();
                schedule.setType(ScheduleType.valueOf(paths.get(1)));
                schedule.setYearRange(paths.get(2));
                schedule.setFaculty(paths.get(3));
                try {
                    schedule.setSemester(paths.get(4));
                } catch (IndexOutOfBoundsException e) {
                    schedule.setSemester(null);
                }
                schedule.setFilename(paths.get(paths.size() - 1));
                schedule.setLink(link);
                var basename = paths.get(paths.size() - 1).split("\\.")[0];
                schedule.setBaseName(basename);
                //        log.info(schedule.toString());
                this.repository.save(schedule);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    private List<String> getPath(String path) {
        String[] paths = path.replaceFirst("/", "").split("/");
        return new ArrayList<>(Arrays.asList(paths));
    }

}
