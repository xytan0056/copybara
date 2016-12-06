import static org.junit.Assert.fail;
import com.google.copybara.Change;
import com.google.copybara.ChangeVisitable.VisitResult;
import com.google.copybara.authoring.Author;
import java.util.ArrayList;
import java.util.List;
  public void defaultPushBranch() throws ValidationException {
    GitDestination d = skylark.eval("result", "result = git.destination('file:///foo')");
    assertThat(d.getPush()).isEqualTo("master");
    assertThat(d.getFetch()).isEqualTo("master");
    options.setForce(true);
    options.setForce(false);
        .contains("\n    \n    " + DummyOrigin.LABEL_NAME + ": " + originRef + "\n");
    thrown.expect(ValidationException.class);
        .matchesNext(MessageType.PROGRESS, "Git Destination: Checking out master")
        .matchesNext(MessageType.WARNING, "Git Destination: Cannot checkout 'FETCH_HEAD'."
            + " Ignoring baseline.")
    Glob firstGlob = new Glob(ImmutableList.of("foo/**", "bar/**"));
    Writer writer1 = destinationFirstCommit().newWriter(firstGlob);
    // Recreate the writer since a destinationFirstCommit writer never looks
    // for a previous ref.
    assertThat(destination().newWriter(firstGlob).getPreviousRef(ref1.getLabelName())).isEqualTo(
        ref1.asString());
    Files.write(workdir.resolve("excluded"), "some content".getBytes());
    // Lets exclude now 'excluded' so that we check that the rebase correctly ignores
    // the missing file (IOW, it doesn't delete the file in the commit).
    destinationFiles = new Glob(ImmutableList.of("**"), ImmutableList.of("excluded"));

    Files.delete(workdir.resolve("excluded"));
        .containsFile("excluded", "some content")

  @Test
  public void testVisit() throws Exception {
    fetch = "master";
    push = "master";
    DummyReference ref1 = new DummyReference("origin_ref1");
    DummyReference ref2 = new DummyReference("origin_ref2");
    Files.write(workdir.resolve("test.txt"), "Visit me".getBytes());
    process(destinationFirstCommit().newWriter(destinationFiles), ref1);
    Files.write(workdir.resolve("test.txt"), "Visit me soon".getBytes());
    process(destination().newWriter(destinationFiles), ref2);

    final List<Change<?>> visited = new ArrayList<>();
    destination().newReader(destinationFiles).visitChanges(null,
        input -> {
          visited.add(input);
          return input.getLabels().get(DummyOrigin.LABEL_NAME).equals("origin_ref1")
              ? VisitResult.TERMINATE
              : VisitResult.CONTINUE;
        });
    assertThat(visited).hasSize(2);
    assertThat(visited.get(0).getLabels().get(DummyOrigin.LABEL_NAME)).isEqualTo("origin_ref2");
    assertThat(visited.get(1).getLabels().get(DummyOrigin.LABEL_NAME)).isEqualTo("origin_ref1");
  }