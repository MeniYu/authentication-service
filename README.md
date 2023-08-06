**Use cases** represent the system's behavior through application-specific operations, which exist within the software
realm to support the domain's constraints.
**Use Cases** may interact directly with entities and other use cases, making them flexible components.

If use cases are just interfaces describing what the software does, we still need to implement the use case interface.
That's the role of the **input port**. By being a component that's directly attached to use cases, at the io.incondensable.Application
level, input ports allow us to implement software intent on domain terms.

There are situations in which a use case needs to fetch data from *external resources* to achieve its goals. That's the
role of **output ports**, which are represented as **interfaces** describing, in a technology-agnostic way, which kind
of data a use case or input port would need to get from outside to perform its operations. I say **agnostic** because **
output ports** *don't care if the data comes from a particular relational database technology or a filesystem*.