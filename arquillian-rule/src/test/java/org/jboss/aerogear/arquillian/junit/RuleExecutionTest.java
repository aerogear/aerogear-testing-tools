package org.jboss.aerogear.arquillian.junit;

import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.arquillian.junit.InSequence;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(ArquillianRules.class)
public class RuleExecutionTest {

    private AtomicInteger methodRuleCounter = new AtomicInteger(0);
    private static AtomicInteger staticMethodRuleCounter = new AtomicInteger(0);
    
    private AtomicInteger testRuleCounter = new AtomicInteger(0);
    private static AtomicInteger staticTestRuleCounter = new AtomicInteger(0);

    @ArquillianRule
    public MethodRule simpleMethodRule = new MethodRule() {

        @Override
        public Statement apply(final Statement base, FrameworkMethod method, Object target) {

            return new Statement() {
                public void evaluate() throws Throwable {
                    methodRuleCounter.incrementAndGet();
                    staticMethodRuleCounter.incrementAndGet();
                    base.evaluate();
                    methodRuleCounter.incrementAndGet();
                    staticMethodRuleCounter.incrementAndGet();
                };
            };
        }
    };

    @ArquillianRule
    public TestRule simpleTestRule = new TestRule() {

        @Override
        public Statement apply(final Statement base, Description description) {

            return new Statement() {
                public void evaluate() throws Throwable {
                    testRuleCounter.incrementAndGet();
                    staticTestRuleCounter.incrementAndGet();
                    base.evaluate();
                    testRuleCounter.incrementAndGet();
                    staticTestRuleCounter.incrementAndGet();
                };
            };
        }
    };

    @Test
    @InSequence(1)
    public void applyRuleOnce() throws Exception {
        Assert.assertThat(methodRuleCounter, is(notNullValue()));
        Assert.assertThat(methodRuleCounter.get(), is(1));
        Assert.assertThat(staticMethodRuleCounter, is(notNullValue()));
        Assert.assertThat(staticMethodRuleCounter.get(), is(1));
        Assert.assertThat(testRuleCounter, is(notNullValue()));
        Assert.assertThat(testRuleCounter.get(), is(1));
        Assert.assertThat(staticTestRuleCounter, is(notNullValue()));
        Assert.assertThat(staticTestRuleCounter.get(), is(1));
    }

    @Test
    @InSequence(2)
    public void applyRuleSecond() throws Exception {
        Assert.assertThat(methodRuleCounter, is(notNullValue()));
        Assert.assertThat(methodRuleCounter.get(), is(1));
        Assert.assertThat(staticMethodRuleCounter, is(notNullValue()));
        Assert.assertThat(staticMethodRuleCounter.get(), is(3));
        Assert.assertThat(testRuleCounter, is(notNullValue()));
        Assert.assertThat(testRuleCounter.get(), is(1));
        Assert.assertThat(staticTestRuleCounter, is(notNullValue()));
        Assert.assertThat(staticTestRuleCounter.get(), is(3));
    }

}