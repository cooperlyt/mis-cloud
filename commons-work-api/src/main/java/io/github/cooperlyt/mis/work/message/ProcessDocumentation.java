package io.github.cooperlyt.mis.work.message;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ProcessDocumentation {

  public static ProcessDocumentationBuilder builder() {
    return new ProcessDocumentationBuilder();
  }

  public static class ProcessDocumentationBuilder {

    @Getter
    private String description;
    private List<String> tags = new ArrayList<>();
    private String keywords;

    ProcessDocumentationBuilder() {
    }


    public ProcessDocumentation.ProcessDocumentationBuilder description(String description) {
      this.description = description;
      return this;
    }

    public ProcessDocumentation.ProcessDocumentationBuilder tags(List<String> tags) {
      this.tags = tags;
      return this;
    }

    public ProcessDocumentation.ProcessDocumentationBuilder putTag(String tag) {
      this.tags.add(tag);
      return this;
    }

    public ProcessDocumentation.ProcessDocumentationBuilder keywords(String keywords) {
      this.keywords = keywords;
      return this;
    }

    private void putKeyword(String keyword) {
      if (this.keywords == null) {
        this.keywords = "";
      }
      this.keywords += keyword + " ";
    }

    public ProcessDocumentation.ProcessDocumentationBuilder putKeywords(String... keywords) {
      for (String keyword : keywords) {
        putKeyword(keyword);
      }
      return this;
    }

    public ProcessDocumentation build() {
      return new ProcessDocumentation(this.description, this.tags, this.keywords);
    }

  }

  private String description;

  private List<String> tags;

  private String keywords;



}
